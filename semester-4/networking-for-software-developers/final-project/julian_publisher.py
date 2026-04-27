import random
import tkinter as tk

from julian_data_generator import SensorDataGenerator
from julian_util import HumidityPacketFactory
from julian_publisher_client import PublisherClient

class PublisherGUI(tk.Tk):
    WINDOW_TITLE = "Humidity Publisher, by Julian Sellanes"
    WINDOW_WIDTH = 860
    WINDOW_HEIGHT = 700
    WINDOW_BG = "#f3f6fb"

    TITLE_FONT = ("Arial", 18, "bold")
    LABEL_FONT = ("Arial", 10)
    ENTRY_FONT = ("Arial", 10)
    STATUS_FONT = ("Arial", 11, "bold")
    TEXT_FONT = ("Consolas", 10)

    CARD_BG = "#ffffff"
    CARD_BORDER = "#d2dae6"
    TEXT_COLOR = "#1f2937"
    MUTED_TEXT = "#4b5563"
    GOOD_COLOR = "#0f766e"
    BAD_COLOR = "#b42318"

    def __init__(self):
        super().__init__()
        self.title(self.WINDOW_TITLE)
        self.geometry(f"{self.WINDOW_WIDTH}x{self.WINDOW_HEIGHT}")
        self.configure(bg=self.WINDOW_BG)
        self.resizable(False, False)

        self._client = None
        self._factory = None
        self._generator = None
        self._running = False
        self._after_id = None
        self._block_skip_remaining = 0

        self._sent_count = 0
        self._missed_count = 0
        self._wild_count = 0
        self._last_packet_id = "-"

        self.status_var = tk.StringVar(value="Stopped")
        self.stats_var = tk.StringVar(value="Sent: 0 | Missed: 0 | Wild: 0 | Last ID: -")

        self._build_ui()

    def _build_ui(self):
        title = tk.Label(
            self,
            text="Humidity Publisher",
            font=self.TITLE_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR
        )
        title.pack(pady=(12, 8))

        self._build_connection_card()
        self._build_publish_card()
        self._build_generator_card()
        self._build_controls_card()
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

        tk.Label(frame, text="Publisher Name", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=0, sticky="w")
        tk.Label(frame, text="Broker", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=2, sticky="w")
        tk.Label(frame, text="Port", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=4, sticky="w")
        tk.Label(frame, text="Topic", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=0, sticky="w", pady=(10, 0))

        self.name_entry = tk.Entry(frame, width=18, font=self.ENTRY_FONT)
        self.name_entry.grid(row=0, column=1, padx=(8, 20))
        self.name_entry.insert(0, "device-1")

        self.broker_entry = tk.Entry(frame, width=18, font=self.ENTRY_FONT)
        self.broker_entry.grid(row=0, column=3, padx=(8, 20))
        self.broker_entry.insert(0, "localhost")

        self.port_entry = tk.Entry(frame, width=8, font=self.ENTRY_FONT)
        self.port_entry.grid(row=0, column=5, padx=(8, 0))
        self.port_entry.insert(0, "1883")

        self.topic_entry = tk.Entry(frame, width=40, font=self.ENTRY_FONT)
        self.topic_entry.grid(row=1, column=1, columnspan=5, sticky="w", padx=(8, 0), pady=(10, 0))
        self.topic_entry.insert(0, "julian/humidity")

    def _build_publish_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        tk.Label(frame, text="Interval (seconds)", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=0, sticky="w")
        tk.Label(frame, text="Miss chance", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=2, sticky="w")
        tk.Label(frame, text="Block skip chance", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=4, sticky="w")
        tk.Label(frame, text="Wild data chance", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=6, sticky="w")
        tk.Label(frame, text="Location", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=1, column=0, sticky="w", pady=(10, 0))

        self.interval_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.interval_entry.grid(row=0, column=1, padx=(8, 20))
        self.interval_entry.insert(0, "1.0")

        self.miss_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.miss_entry.grid(row=0, column=3, padx=(8, 20))
        self.miss_entry.insert(0, "0.01")

        self.block_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.block_entry.grid(row=0, column=5, padx=(8, 20))
        self.block_entry.insert(0, "0.01")

        self.corrupt_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.corrupt_entry.grid(row=0, column=7, padx=(8, 0))
        self.corrupt_entry.insert(0, "0.03")

        self.location_entry = tk.Entry(frame, width=45, font=self.ENTRY_FONT)
        self.location_entry.grid(row=1, column=1, columnspan=7, sticky="w", padx=(8, 0), pady=(10, 0))
        self.location_entry.insert(0, "Toronto Campus Outdoors in Winter")

    def _build_generator_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        tk.Label(frame, text="Minimum", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=0, sticky="w")
        tk.Label(frame, text="Maximum", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=2, sticky="w")
        tk.Label(frame, text="Baseline", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=4, sticky="w")
        tk.Label(frame, text="Spread", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).grid(row=0, column=6, sticky="w")

        self.min_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.min_entry.grid(row=0, column=1, padx=(8, 20))
        self.min_entry.insert(0, "58.0")

        self.max_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.max_entry.grid(row=0, column=3, padx=(8, 20))
        self.max_entry.insert(0, "90.0")

        self.baseline_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.baseline_entry.grid(row=0, column=5, padx=(8, 20))
        self.baseline_entry.insert(0, "0.50")

        self.spread_entry = tk.Entry(frame, width=10, font=self.ENTRY_FONT)
        self.spread_entry.grid(row=0, column=7, padx=(8, 0))
        self.spread_entry.insert(0, "0.18")

    def _build_controls_card(self):
        frame = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            padx=12,
            pady=12
        )
        frame.pack(fill="x", padx=16, pady=(0, 10))

        start_btn = tk.Button(frame, text="Start", width=14, command=self._start_publishing)
        start_btn.grid(row=0, column=0, padx=(0, 10))

        stop_btn = tk.Button(frame, text="Stop", width=14, command=self._stop_publishing)
        stop_btn.grid(row=0, column=1, padx=(0, 10))

        once_btn = tk.Button(frame, text="Send Once", width=14, command=self._send_once)
        once_btn.grid(row=0, column=2, padx=(0, 16))

        self.status_label = tk.Label(
            frame,
            textvariable=self.status_var,
            font=self.STATUS_FONT,
            bg=self.CARD_BG,
            fg=self.BAD_COLOR,
            anchor="w",
            justify="left",
            wraplength=500,
            width=45
        )
        self.status_label.grid(row=0, column=3, sticky="w")

        stats = tk.Label(
            frame,
            textvariable=self.stats_var,
            font=self.LABEL_FONT,
            bg=self.CARD_BG,
            fg=self.MUTED_TEXT
        )
        stats.grid(row=1, column=0, columnspan=4, sticky="w", pady=(10, 0))

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

        tk.Label(frame, text="Publisher Log", font=self.LABEL_FONT, bg=self.CARD_BG, fg=self.TEXT_COLOR).pack(anchor="w")

        self.log_text = tk.Text(frame, height=16, font=self.TEXT_FONT, bg="#f8fafc", fg=self.TEXT_COLOR)
        self.log_text.pack(fill="both", expand=True, pady=(8, 0))

    def _read_float(self, entry_widget, field_name):
        text = entry_widget.get().strip()
        try:
            return float(text)
        except ValueError:
            raise ValueError(f"{field_name} must be numeric.")

    def _read_int(self, entry_widget, field_name):
        text = entry_widget.get().strip()
        try:
            return int(text)
        except ValueError:
            raise ValueError(f"{field_name} must be an integer.")

    def _load_settings(self):
        minimum = self._read_float(self.min_entry, "Minimum")
        maximum = self._read_float(self.max_entry, "Maximum")
        baseline = self._read_float(self.baseline_entry, "Baseline")
        spread = self._read_float(self.spread_entry, "Spread")
        port = self._read_int(self.port_entry, "Port")
        interval = self._read_float(self.interval_entry, "Interval")
        miss_chance = self._read_float(self.miss_entry, "Miss chance")
        block_chance = self._read_float(self.block_entry, "Block skip chance")
        corrupt_chance = self._read_float(self.corrupt_entry, "Wild data chance")

        if minimum >= maximum:
            raise ValueError("Minimum must be less than maximum.")
        if interval <= 0:
            raise ValueError("Interval must be greater than 0.")
        if not (0 <= baseline <= 1):
            raise ValueError("Baseline must be between 0 and 1.")
        if spread <= 0:
            raise ValueError("Spread must be greater than 0.")
        if not (0 <= miss_chance <= 1):
            raise ValueError("Miss chance must be between 0 and 1.")
        if not (0 <= block_chance <= 1):
            raise ValueError("Block skip chance must be between 0 and 1.")
        if not (0 <= corrupt_chance <= 1):
            raise ValueError("Wild data chance must be between 0 and 1.")

        return {
            "publisher_name": self.name_entry.get().strip() or "device-1",
            "broker": self.broker_entry.get().strip() or "localhost",
            "port": port,
            "topic": self.topic_entry.get().strip() or "julian/humidity",
            "interval_ms": max(100, int(interval * 1000)),
            "miss_chance": miss_chance,
            "block_chance": block_chance,
            "corrupt_chance": corrupt_chance,
            "location": self.location_entry.get().strip() or "Unknown Location",
            "minimum": minimum,
            "maximum": maximum,
            "baseline": baseline,
            "spread": spread
        }

    def _create_runtime_objects(self, settings):
        self._generator = SensorDataGenerator(
            minimum=settings["minimum"],
            maximum=settings["maximum"],
            baseline=settings["baseline"],
            spread=settings["spread"],
            min_segment=28,
            max_segment=42,
            drift_factor=0.08,
            wiggle_step=0.007,
            wiggle_limit=0.03,
            jitter=0.002
        )

        self._factory = HumidityPacketFactory(
            start_id=111,
            location=settings["location"]
        )

        self._client = PublisherClient(
            broker=settings["broker"],
            port=settings["port"],
            topic=settings["topic"]
        )

        connected = self._client.connect()
        if not connected:
            raise ConnectionError("Could not connect to the MQTT broker.")

    def _start_publishing(self):
        if self._running:
            self._log("Publisher is already running.")
            return

        try:
            self._settings = self._load_settings()
            self._create_runtime_objects(self._settings)
        except Exception as exc:
            self._set_status("Connection failed. Check broker and port.", is_good=False)
            self._log(f"START FAILED -> {exc}")
            return

        self._running = True
        self._block_skip_remaining = 0
        self._set_status("Running", is_good=True)
        self._log("Publisher started.")
        self._schedule_next()

    def _stop_publishing(self):
        self._running = False

        if self._after_id is not None:
            self.after_cancel(self._after_id)
            self._after_id = None

        if self._client is not None:
            self._client.disconnect()
            self._client = None

        self._set_status("Stopped", is_good=False)
        self._log("Publisher stopped.")

    def _schedule_next(self):
        if self._running:
            self._after_id = self.after(self._settings["interval_ms"], self._tick)

    def _tick(self):
        self._produce_cycle()
        self._schedule_next()

    def _send_once(self):
        if self._running:
            self._log("Stop the publisher before using Send Once.")
            return

        try:
            self._settings = self._load_settings()
            self._create_runtime_objects(self._settings)
            self._produce_cycle()
        except Exception as exc:
            self._set_status("Connection failed. Check broker and port.", is_good=False)
            self._log(f"SEND ONCE FAILED -> {exc}")
        finally:
            if self._client is not None:
                self._client.disconnect()
                self._client = None
            self._set_status("Stopped", is_good=False)

    def _produce_cycle(self):
        packet_id = self._factory.reserve_packet_id()

        if self._block_skip_remaining > 0:
            self._block_skip_remaining -= 1
            self._missed_count += 1
            self._last_packet_id = packet_id
            self._update_stats()
            self._log(f"BLOCK SKIP -> packet {packet_id} intentionally not sent")
            return

        if random.random() < self._settings["block_chance"]:
            self._block_skip_remaining = random.randint(2, 4) - 1
            self._missed_count += 1
            self._last_packet_id = packet_id
            self._update_stats()
            self._log(f"BLOCK SKIP START -> packet {packet_id} not sent")
            return

        if random.random() < self._settings["miss_chance"]:
            self._missed_count += 1
            self._last_packet_id = packet_id
            self._update_stats()
            self._log(f"MISS -> packet {packet_id} intentionally not sent")
            return

        humidity = round(self._generator.reading, 1)
        is_corrupt = False

        if random.random() < self._settings["corrupt_chance"]:
            humidity = self._mutate_value(humidity)
            is_corrupt = True
            self._wild_count += 1

        packet = self._factory.build_packet(
            packet_id=packet_id,
            humidity_value=humidity,
            publisher_name=self._settings["publisher_name"],
            is_corrupt=is_corrupt
        )

        payload_string = self._factory.to_json(packet)
        ok = self._client.publish(payload_string)

        self._last_packet_id = packet_id

        if ok:
            self._sent_count += 1
            self._update_stats()
            self._log(
                f"SENT -> id={packet_id}, humidity={packet['humidity']}%, "
                f"status={packet['status']}"
            )
        else:
            self._log(f"PUBLISH FAILED -> packet {packet_id}")

    def _mutate_value(self, value):
        if random.choice([True, False]):
            return round(value - random.uniform(15.0, 28.0), 1)
        return round(value + random.uniform(15.0, 28.0), 1)

    def _update_stats(self):
        self.stats_var.set(
            f"Sent: {self._sent_count} | Missed: {self._missed_count} | "
            f"Wild: {self._wild_count} | Last ID: {self._last_packet_id}"
        )

    def _set_status(self, message, is_good):
        self.status_var.set(message)
        self.status_label.config(fg=self.GOOD_COLOR if is_good else self.BAD_COLOR)

    def _log(self, message):
        self.log_text.insert("end", f"{message}\n")
        self.log_text.see("end")

if __name__ == "__main__":
    app = PublisherGUI()
    app.mainloop()