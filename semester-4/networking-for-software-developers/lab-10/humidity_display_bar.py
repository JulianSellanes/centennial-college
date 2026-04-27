import tkinter as tk
from julian_data_generator import SensorDataGenerator

# Julian Sellanes (301494667)

class HumidityBarDisplay(tk.Tk):
    WINDOW_TITLE = "Humidity Bar Display"
    WINDOW_WIDTH = 760
    WINDOW_HEIGHT = 520
    WINDOW_BG = "#f4f7fb"

    TITLE_FONT = ("Arial", 20, "bold")
    SUBTITLE_FONT = ("Arial", 11)
    VALUE_FONT = ("Arial", 28, "bold")
    LABEL_FONT = ("Arial", 11)
    INFO_FONT = ("Arial", 10)
    STATUS_FONT = ("Arial", 12, "bold")

    CARD_BG = "#ffffff"
    CARD_BORDER = "#d6deeb"
    TEXT_COLOR = "#1f2937"
    SECONDARY_TEXT = "#4b5563"
    ERROR_COLOR = "#b42318"
    SUCCESS_COLOR = "#0f766e"

    BAR_LEFT = 70
    BAR_TOP = 60
    BAR_WIDTH = 560
    BAR_HEIGHT = 48
    BAR_BOTTOM_GAP = 34
    MARKER_HEIGHT = 12
    TICK_HEIGHT = 10

    SCALE_MIN = 58.0
    SCALE_MAX = 90.0
    NORMAL_LOW = 60.0
    NORMAL_HIGH = 85.0
    INITIAL_VALUE = 74.0

    LOW_BG = "#fde68a"
    NORMAL_BG = "#bbf7d0"
    HIGH_BG = "#fecaca"
    BAR_FILL_LOW = "#f59e0b"
    BAR_FILL_NORMAL = "#10b981"
    BAR_FILL_HIGH = "#ef4444"
    BAR_OUTLINE = "#334155"
    MARKER_COLOR = "#111827"

    SENSOR_MINIMUM = 58.0
    SENSOR_MAXIMUM = 90.0
    SENSOR_BASELINE = 0.50
    SENSOR_SPREAD = 0.18
    SENSOR_MIN_SEGMENT = 28
    SENSOR_MAX_SEGMENT = 42
    SENSOR_DRIFT_FACTOR = 0.08
    SENSOR_WIGGLE_STEP = 0.007
    SENSOR_WIGGLE_LIMIT = 0.03
    SENSOR_JITTER = 0.002

    def __init__(self):
        super().__init__()
        self.title(self.WINDOW_TITLE)
        self.geometry(f"{self.WINDOW_WIDTH}x{self.WINDOW_HEIGHT}")
        self.configure(bg=self.WINDOW_BG)
        self.resizable(False, False)

        self.sensor = SensorDataGenerator(
            minimum=self.SENSOR_MINIMUM,
            maximum=self.SENSOR_MAXIMUM,
            baseline=self.SENSOR_BASELINE,
            spread=self.SENSOR_SPREAD,
            min_segment=self.SENSOR_MIN_SEGMENT,
            max_segment=self.SENSOR_MAX_SEGMENT,
            drift_factor=self.SENSOR_DRIFT_FACTOR,
            wiggle_step=self.SENSOR_WIGGLE_STEP,
            wiggle_limit=self.SENSOR_WIGGLE_LIMIT,
            jitter=self.SENSOR_JITTER,
        )

        self.current_value = self.INITIAL_VALUE
        self.message_var = tk.StringVar(
            value=(
                f"Enter a humidity value from {self.SCALE_MIN:.1f} to "
                f"{self.SCALE_MAX:.1f}."
            )
        )

        self._build_widgets()
        self._update_display(self.current_value)

    def _build_widgets(self):
        title_label = tk.Label(
            self,
            text="Outdoor Humidity Near a Toronto Campus in Winter",
            font=self.TITLE_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR,
        )
        title_label.pack(pady=(18, 4))

        subtitle_label = tk.Label(
            self,
            text="by: Julian Sellanes",
            font=self.SUBTITLE_FONT,
            bg=self.WINDOW_BG,
            fg=self.SECONDARY_TEXT,
        )
        subtitle_label.pack(pady=(0, 12))

        top_card = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            bd=0,
            padx=18,
            pady=18,
        )
        top_card.pack(padx=22, pady=(0, 14), fill="x")

        self.value_label = tk.Label(
            top_card,
            text="",
            font=self.VALUE_FONT,
            bg=self.CARD_BG,
            fg=self.TEXT_COLOR,
        )
        self.value_label.pack(anchor="center", pady=(0, 4))

        self.status_label = tk.Label(
            top_card,
            text="",
            font=self.STATUS_FONT,
            bg=self.CARD_BG,
            fg=self.SUCCESS_COLOR,
        )
        self.status_label.pack(anchor="center", pady=(0, 8))

        self.canvas = tk.Canvas(
            top_card,
            width=self.BAR_LEFT + self.BAR_WIDTH + self.BAR_LEFT,
            height=160,
            bg=self.CARD_BG,
            highlightthickness=0,
        )
        self.canvas.pack()

        info_label = tk.Label(
            top_card,
            text=(
                f"Units: % RH      Low: below {self.NORMAL_LOW:.0f}%      "
                f"Normal: {self.NORMAL_LOW:.0f}% - {self.NORMAL_HIGH:.0f}%      "
                f"High: above {self.NORMAL_HIGH:.0f}%"
            ),
            font=self.INFO_FONT,
            bg=self.CARD_BG,
            fg=self.SECONDARY_TEXT,
        )
        info_label.pack(pady=(8, 0))

        bottom_card = tk.Frame(
            self,
            bg=self.CARD_BG,
            highlightbackground=self.CARD_BORDER,
            highlightthickness=1,
            bd=0,
            padx=18,
            pady=18,
        )
        bottom_card.pack(padx=22, pady=(0, 18), fill="x")

        entry_label = tk.Label(
            bottom_card,
            text="Set humidity value (%):",
            font=self.LABEL_FONT,
            bg=self.CARD_BG,
            fg=self.TEXT_COLOR,
        )
        entry_label.grid(row=0, column=0, sticky="w", padx=(0, 10), pady=(0, 10))

        self.value_entry = tk.Entry(
            bottom_card,
            width=12,
            font=self.LABEL_FONT,
            justify="center",
            fg=self.TEXT_COLOR,
            bg="#ffffff",
        )
        self.value_entry.grid(row=0, column=1, sticky="w", pady=(0, 10))
        self.value_entry.insert(0, f"{self.INITIAL_VALUE:.1f}")

        apply_button = tk.Button(
            bottom_card,
            text="Update Display",
            font=self.LABEL_FONT,
            width=16,
            command=self._apply_manual_value,
            bg="#e2e8f0",
            fg=self.TEXT_COLOR,
        )
        apply_button.grid(row=0, column=2, padx=(12, 8), pady=(0, 10))

        sensor_button = tk.Button(
            bottom_card,
            text="Read Sensor",
            font=self.LABEL_FONT,
            width=14,
            command=self._use_sensor_value,
            bg="#dbeafe",
            fg=self.TEXT_COLOR,
        )
        sensor_button.grid(row=0, column=3, pady=(0, 10))

        self.message_label = tk.Label(
            bottom_card,
            textvariable=self.message_var,
            font=self.INFO_FONT,
            bg=self.CARD_BG,
            fg=self.SECONDARY_TEXT,
        )
        self.message_label.grid(row=1, column=0, columnspan=4, sticky="w")

    def _apply_manual_value(self):
        entry_text = self.value_entry.get().strip()

        if entry_text == "":
            self._set_message("Please enter a value.", is_error=True)
            return

        try:
            new_value = float(entry_text)
        except ValueError:
            self._set_message("Invalid input. Enter a numeric humidity value.", is_error=True)
            return

        if new_value < self.SCALE_MIN or new_value > self.SCALE_MAX:
            self._set_message(
                f"Value must be between {self.SCALE_MIN:.1f} and {self.SCALE_MAX:.1f}.",
                is_error=True,
            )
            return

        self._update_display(new_value)
        self._set_message("Display updated successfully.", is_error=False)

    def _use_sensor_value(self):
        sensor_value = round(self.sensor.reading, 1)
        self.value_entry.delete(0, tk.END)
        self.value_entry.insert(0, f"{sensor_value:.1f}")
        self._update_display(sensor_value)
        self._set_message("Display updated from the sensor generator.", is_error=False)

    def _update_display(self, value):
        self.current_value = value
        status_text, status_color = self._get_status(value)
        fill_color = self._get_fill_color(value)

        self.value_label.config(text=f"{value:.1f} % RH")
        self.status_label.config(text=status_text, fg=status_color)

        self.canvas.delete("all")

        low_end_x = self._value_to_x(self.NORMAL_LOW)
        normal_end_x = self._value_to_x(self.NORMAL_HIGH)
        current_x = self._value_to_x(value)

        self.canvas.create_rectangle(
            self.BAR_LEFT,
            self.BAR_TOP,
            low_end_x,
            self.BAR_TOP + self.BAR_HEIGHT,
            fill=self.LOW_BG,
            outline="",
        )
        self.canvas.create_rectangle(
            low_end_x,
            self.BAR_TOP,
            normal_end_x,
            self.BAR_TOP + self.BAR_HEIGHT,
            fill=self.NORMAL_BG,
            outline="",
        )
        self.canvas.create_rectangle(
            normal_end_x,
            self.BAR_TOP,
            self.BAR_LEFT + self.BAR_WIDTH,
            self.BAR_TOP + self.BAR_HEIGHT,
            fill=self.HIGH_BG,
            outline="",
        )

        self.canvas.create_rectangle(
            self.BAR_LEFT,
            self.BAR_TOP,
            current_x,
            self.BAR_TOP + self.BAR_HEIGHT,
            fill=fill_color,
            outline="",
        )

        self.canvas.create_rectangle(
            self.BAR_LEFT,
            self.BAR_TOP,
            self.BAR_LEFT + self.BAR_WIDTH,
            self.BAR_TOP + self.BAR_HEIGHT,
            outline=self.BAR_OUTLINE,
            width=2,
        )

        self.canvas.create_line(
            current_x,
            self.BAR_TOP - self.MARKER_HEIGHT,
            current_x,
            self.BAR_TOP + self.BAR_HEIGHT + self.MARKER_HEIGHT,
            fill=self.MARKER_COLOR,
            width=2,
        )

        self._draw_scale_label(self.SCALE_MIN, f"{self.SCALE_MIN:.0f}")
        self._draw_scale_label(self.NORMAL_LOW, f"{self.NORMAL_LOW:.0f}")
        self._draw_scale_label(self.NORMAL_HIGH, f"{self.NORMAL_HIGH:.0f}")
        self._draw_scale_label(self.SCALE_MAX, f"{self.SCALE_MAX:.0f}")

        self.canvas.create_text(
            (self.BAR_LEFT + low_end_x) / 2,
            self.BAR_TOP + self.BAR_HEIGHT / 2,
            text="LOW",
            font=self.INFO_FONT,
            fill=self.TEXT_COLOR,
        )
        self.canvas.create_text(
            (low_end_x + normal_end_x) / 2,
            self.BAR_TOP + self.BAR_HEIGHT / 2,
            text="NORMAL",
            font=self.INFO_FONT,
            fill=self.TEXT_COLOR,
        )
        self.canvas.create_text(
            (normal_end_x + self.BAR_LEFT + self.BAR_WIDTH) / 2,
            self.BAR_TOP + self.BAR_HEIGHT / 2,
            text="HIGH",
            font=self.INFO_FONT,
            fill=self.TEXT_COLOR,
        )

    def _draw_scale_label(self, value, label_text):
        x = self._value_to_x(value)
        y1 = self.BAR_TOP + self.BAR_HEIGHT
        y2 = y1 + self.TICK_HEIGHT
        text_y = y2 + self.BAR_BOTTOM_GAP / 2

        self.canvas.create_line(x, y1, x, y2, fill=self.BAR_OUTLINE, width=2)
        self.canvas.create_text(x, text_y, text=label_text, font=self.INFO_FONT, fill=self.TEXT_COLOR)

    def _value_to_x(self, value):
        scale = (value - self.SCALE_MIN) / (self.SCALE_MAX - self.SCALE_MIN)
        return self.BAR_LEFT + (scale * self.BAR_WIDTH)

    def _get_status(self, value):
        if value < self.NORMAL_LOW:
            return "LOW HUMIDITY", "#b45309"
        if value <= self.NORMAL_HIGH:
            return "NORMAL HUMIDITY", self.SUCCESS_COLOR
        return "HIGH HUMIDITY", self.ERROR_COLOR

    def _get_fill_color(self, value):
        if value < self.NORMAL_LOW:
            return self.BAR_FILL_LOW
        if value <= self.NORMAL_HIGH:
            return self.BAR_FILL_NORMAL
        return self.BAR_FILL_HIGH

    def _set_message(self, message, is_error):
        self.message_var.set(message)
        self.message_label.config(
            fg=self.ERROR_COLOR if is_error else self.SECONDARY_TEXT
        )

if __name__ == "__main__":
    app = HumidityBarDisplay()
    app.mainloop()