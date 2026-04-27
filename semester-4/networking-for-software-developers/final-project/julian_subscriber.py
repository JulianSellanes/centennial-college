import tkinter as tk

from julian_util import HumidityPacketFactory
from julian_subscriber_client import SubscriberClient

class SubscriberGUI(tk.Tk):
    WINDOW_TITLE = "Humidity Subscriber, by Julian Sellanes"
    WINDOW_WIDTH = 920
    WINDOW_HEIGHT = 860
    WINDOW_BG = "#f3f6fb"

    TITLE_FONT = ("Arial", 18, "bold")
    LABEL_FONT = ("Arial", 10)
    VALUE_FONT = ("Arial", 14, "bold")
    ENTRY_FONT = ("Arial", 10)
    TEXT_FONT = ("Consolas", 10)

    CARD_BG = "#ffffff"
    CARD_BORDER = "#d2dae6"
    TEXT_COLOR = "#1f2937"
    MUTED_TEXT = "#4b5563"
    GOOD_COLOR = "#0f766e"
    BAD_COLOR = "#b42318"
    WARN_COLOR = "#b45309"

    CANVAS_WIDTH = 840
    CANVAS_HEIGHT = 280
    CHART_LEFT = 50
    CHART_RIGHT = 20
    CHART_TOP = 25
    CHART_BOTTOM = 220

    DISPLAY_MIN = 45.0
    DISPLAY_MAX = 105.0
    NORMAL_MIN = 58.0
    NORMAL_MAX = 90.0
    MAX_HISTORY = 20

    def __init__(self):
        super().__init__()
        self.title(self.WINDOW_TITLE)
        self.geometry(f"{self.WINDOW_WIDTH}x{self.WINDOW_HEIGHT}")
        self.configure(bg=self.WINDOW_BG)
        self.resizable(False, False)

        self._client = None
        self._factory = HumidityPacketFactory()
        self._history = []
        self._last_id_by_publisher = {}

        self._received_count = 0
        self._missing_count = 0
        self._wild_count = 0

        self.status_var = tk.StringVar(value="Disconnected")
        self.alert_var = tk.StringVar(value="No alerts yet.")
        self.summary_var = tk.StringVar(value="Received: 0 | Missing: 0 | Wild: 0")

        self.publisher_var = tk.StringVar(value="-")
        self.packet_var = tk.StringVar(value="-")
        self.time_var = tk.StringVar(value="-")
        self.humidity_var = tk.StringVar(value="-")
        self.packet_status_var = tk.StringVar(value="-")

        self._build_ui()
        self._draw_chart()

    def _build_ui(self):
        title = tk.Label(
            self,
            text="Humidity Subscriber",
            font=self.TITLE_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR
        )
        title.pack(pady=(12, 8))

        self._build_connection_card()
        self._build_info_card()
        self._build_chart_card()
        self._build_log_card()

    def _build_connection_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        tk.Label(frame, text="Broker", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=0, sticky="w")
        tk.Label(frame, text="Port", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=2, sticky="w")
        tk.Label(frame, text="Topic", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=4, sticky="w")

        self.broker_entry = tk.Entry(frame, width=18, font=self.ENTRY_FONT)
        self.broker_entry.grid(row=0, column=1, padx=(8, 20))
        self.broker_entry.insert(0, "localhost")

        self.port_entry = tk.Entry(frame, width=8, font=self.ENTRY_FONT)
        self.port_entry.grid(row=0, column=3, padx=(8, 20))
        self.port_entry.insert(0, "1883")

        self.topic_entry = tk.Entry(frame, width=34, font=self.ENTRY_FONT)
        self.topic_entry.grid(row=0, column=5, padx=(8, 20))
        self.topic_entry.insert(0, "julian/humidity")

        connect_btn = tk.Button(frame, text="Connect", width=12, command=self._connect)
        connect_btn.grid(row=0, column=6, padx=(0, 10))

        disconnect_btn = tk.Button(frame, text="Disconnect", width=12, command=self._disconnect)
        disconnect_btn.grid(row=0, column=7)

        self.status_label = tk.Label(
            frame,
            textvariable=self.status_var,
            font=self.VALUE_FONT,
            bg=self.CARD_BG,
            fg=self.BAD_COLOR,
            anchor="w",
            justify="left",
            wraplength=500,
            width=45
        )
        self.status_label.grid(row=1, column=0, columnspan=8, sticky="w", pady=(10, 0))

    def _build_info_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        tk.Label(frame, text="Latest Publisher:", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=0, sticky="w")
        tk.Label(frame, textvariable=self.publisher_var, font=self.VALUE_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=1, sticky="w", padx=(8, 20))

        tk.Label(frame, text="Packet ID:", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=2, sticky="w")
        tk.Label(frame, textvariable=self.packet_var, font=self.VALUE_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=3, sticky="w", padx=(8, 20))

        tk.Label(frame, text="Humidity:", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=0, sticky="w", pady=(10, 0))
        tk.Label(frame, textvariable=self.humidity_var, font=self.VALUE_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=1, sticky="w", padx=(8, 20), pady=(10, 0))

        tk.Label(frame, text="Time:", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=2, sticky="w", pady=(10, 0))
        tk.Label(frame, textvariable=self.time_var, font=self.VALUE_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=3, sticky="w", padx=(8, 20), pady=(10, 0))

        tk.Label(frame, text="Packet Status:", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=2, column=0, sticky="w", pady=(10, 0))
        tk.Label(frame, textvariable=self.packet_status_var, font=self.VALUE_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=2, column=1, sticky="w", padx=(8, 20), pady=(10, 0))

        self.alert_label = tk.Label(
            frame,
            textvariable=self.alert_var,
            font=self.VALUE_FONT,
            bg=self.CARD_BG,
            fg=self.WARN_COLOR,
            wraplength=820,
            justify="left"
        )
        self.alert_label.grid(row=3, column=0, columnspan=4, sticky="w", pady=(12, 0))

        summary = tk.Label(
            frame,
            textvariable=self.summary_var,
            font=self.LABEL_FONT,
            bg=self.CARD_BG,
            fg=self.MUTED_TEXT
        )
        summary.grid(row=4, column=0, columnspan=4, sticky="w", pady=(10, 0))

    def _build_chart_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        tk.Label(frame, text="Canvas Chart (latest 20 values)", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).pack(anchor="w")

        self.canvas = tk.Canvas(
            frame,
            width=self.CANVAS_WIDTH,
            height=self.CANVAS_HEIGHT,
            bg="#f8fafc",
            highlightthickness=0
        )
        self.canvas.pack(pady=(8, 0))

    def _build_log_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="both", expand=True, padx=16, pady=(0, 16))

        tk.Label(frame, text="Subscriber Log", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).pack(anchor="w")

        self.log_text = tk.Text(frame, height=12, font=self.TEXT_FONT, bg="#f8fafc", fg=self.TEXT_COLOR)
        self.log_text.pack(fill="both", expand=True, pady=(8, 0))

    def _connect(self):
        try:
            broker = self.broker_entry.get().strip() or "localhost"
            port = int(self.port_entry.get().strip())
            topic = self.topic_entry.get().strip() or "julian/humidity"

            self._client = SubscriberClient(broker=broker, port=port, topic=topic)
            self._client.set_message_callback(self._message_arrived_from_thread)

            connected = self._client.connect()
            if not connected:
                raise ConnectionError("Could not connect to the MQTT broker.")

            self._set_status("Connected", is_good=True)
            self._log(f"Connected to {broker}:{port}, topic={topic}")
        except Exception as exc:
            self._set_status("Connection failed. Check broker and port.", is_good=False)
            self._log(f"CONNECT FAILED -> {exc}")

    def _disconnect(self):
        if self._client is not None:
            self._client.disconnect()
            self._client = None

        self._set_status("Disconnected", is_good=False)
        self._log("Disconnected from broker.")

    def _message_arrived_from_thread(self, payload_string):
        self.after(0, self._process_payload, payload_string)

    def _process_payload(self, payload_string):
        try:
            packet = self._factory.from_json(payload_string)
        except Exception as exc:
            self.alert_var.set(f"Invalid JSON received: {exc}")
            self._log(f"BAD JSON -> {payload_string}")
            return

        publisher = packet.get("publisher", "unknown")
        packet_id = int(packet.get("packet_id", 0))
        humidity = float(packet.get("humidity", 0.0))
        timestamp = packet.get("timestamp", "-")
        packet_status = packet.get("status", "normal")

        self._received_count += 1

        alerts = []

        previous_id = self._last_id_by_publisher.get(publisher)
        if previous_id is not None:
            expected = previous_id + 1
            if packet_id > expected:
                missing_here = packet_id - expected
                self._missing_count += missing_here
                alerts.append(f"Missing {missing_here} packet(s) from {publisher}.")
            elif packet_id <= previous_id:
                alerts.append(f"Out-of-order or duplicate packet from {publisher}.")

        self._last_id_by_publisher[publisher] = packet_id

        is_wild = humidity < self.NORMAL_MIN or humidity > self.NORMAL_MAX
        if is_wild:
            self._wild_count += 1
            alerts.append(
                f"Out-of-range humidity from {publisher}: {humidity}% "
                f"(expected {self.NORMAL_MIN}% to {self.NORMAL_MAX}%)."
            )

        self.publisher_var.set(publisher)
        self.packet_var.set(str(packet_id))
        self.time_var.set(timestamp)
        self.humidity_var.set(f"{humidity:.1f} %")
        self.packet_status_var.set(packet_status.upper())

        if alerts:
            self.alert_var.set(" | ".join(alerts))
        else:
            self.alert_var.set("Data received successfully.")

        self.summary_var.set(
            f"Received: {self._received_count} | Missing: {self._missing_count} | Wild: {self._wild_count}"
        )

        self._history.append({
            "packet_id": packet_id,
            "humidity": humidity,
            "publisher": publisher,
            "wild": is_wild
        })

        if len(self._history) > self.MAX_HISTORY:
            self._history.pop(0)

        self._draw_chart()
        self._log(
            f"RECEIVED -> publisher={publisher}, id={packet_id}, "
            f"humidity={humidity:.1f}, status={packet_status}"
        )

    def _draw_chart(self):
        self.canvas.delete("all")

        left = self.CHART_LEFT
        right = self.CANVAS_WIDTH - self.CHART_RIGHT
        top = self.CHART_TOP
        bottom = self.CHART_BOTTOM
        width = right - left

        normal_top = self._value_to_y(self.NORMAL_MAX)
        normal_bottom = self._value_to_y(self.NORMAL_MIN)

        self.canvas.create_rectangle(left, normal_top, right, normal_bottom, fill="#dcfce7", outline="")
        self.canvas.create_line(left, bottom, right, bottom, fill="#475569", width=2)
        self.canvas.create_line(left, top, left, bottom, fill="#475569", width=2)

        self.canvas.create_text(24, top, text=f"{self.DISPLAY_MAX:.0f}", fill=self.TEXT_COLOR, font=self.LABEL_FONT)
        self.canvas.create_text(24, bottom, text=f"{self.DISPLAY_MIN:.0f}", fill=self.TEXT_COLOR, font=self.LABEL_FONT)
        self.canvas.create_text(28, normal_top, text=f"{self.NORMAL_MAX:.0f}", fill=self.GOOD_COLOR, font=self.LABEL_FONT)
        self.canvas.create_text(28, normal_bottom, text=f"{self.NORMAL_MIN:.0f}", fill=self.GOOD_COLOR, font=self.LABEL_FONT)

        if not self._history:
            self.canvas.create_text(
                self.CANVAS_WIDTH / 2,
                self.CANVAS_HEIGHT / 2,
                text="No data received yet.",
                fill=self.MUTED_TEXT,
                font=self.VALUE_FONT
            )
            return

        bar_gap = 8
        count = len(self._history)
        bar_width = (width - (bar_gap * (count - 1))) / count
        points = []

        for index, item in enumerate(self._history):
            x1 = left + index * (bar_width + bar_gap)
            x2 = x1 + bar_width
            center_x = (x1 + x2) / 2

            y = self._value_to_y(item["humidity"])
            fill = "#ef4444" if item["wild"] else "#60a5fa"

            self.canvas.create_rectangle(
                x1,
                y,
                x2,
                bottom,
                fill=fill,
                outline="#334155",
                width=1
            )

            self.canvas.create_text(
                center_x,
                bottom + 14,
                text=str(item["packet_id"]),
                fill=self.TEXT_COLOR,
                font=("Arial", 8)
            )

            points.extend([center_x, y])

        if len(points) >= 4:
            self.canvas.create_line(points, fill="#0f172a", width=2)

        self.canvas.create_text(
            self.CANVAS_WIDTH / 2,
            self.CANVAS_HEIGHT - 10,
            text="Packet IDs",
            fill=self.TEXT_COLOR,
            font=self.LABEL_FONT
        )

    def _value_to_y(self, value):
        if value < self.DISPLAY_MIN:
            value = self.DISPLAY_MIN
        if value > self.DISPLAY_MAX:
            value = self.DISPLAY_MAX

        usable_height = self.CHART_BOTTOM - self.CHART_TOP
        normalized = (value - self.DISPLAY_MIN) / (self.DISPLAY_MAX - self.DISPLAY_MIN)
        return self.CHART_BOTTOM - (normalized * usable_height)

    def _set_status(self, message, is_good):
        self.status_var.set(message)
        self.status_label.config(fg=self.GOOD_COLOR if is_good else self.BAD_COLOR)

    def _log(self, message):
        self.log_text.insert("end", f"{message}\n")
        self.log_text.see("end")

if __name__ == "__main__":
    app = SubscriberGUI()
    app.mainloop()