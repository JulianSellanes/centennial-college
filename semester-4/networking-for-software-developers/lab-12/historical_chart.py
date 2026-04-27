import tkinter as tk
from julian_data_generator import SensorDataGenerator

# Julian Sellanes (301494667)

class HumidityHistoricalChart(tk.Tk):
    WINDOW_TITLE = "Historical Data, By: Julian"
    WINDOW_WIDTH = 640
    WINDOW_HEIGHT = 430
    WINDOW_BG = "#ececec"

    APP_TITLE_FONT = ("Arial", 18, "bold")
    LABEL_FONT = ("Arial", 11)
    RANGE_FONT = ("Arial", 14)
    INFO_FONT = ("Arial", 10)

    TOTAL_VALUES = 20
    VISIBLE_VALUES = 6
    MIN_START_INDEX = 0
    MAX_START_INDEX = TOTAL_VALUES - VISIBLE_VALUES

    CANVAS_WIDTH = 600
    CANVAS_HEIGHT = 300
    CHART_LEFT = 18
    CHART_RIGHT = 18
    CHART_TOP = 25
    CHART_BOTTOM = 250
    BAR_GAP = 14
    BAR_COLOR = "#8dc68d"
    BAR_OUTLINE = "#2f2f2f"
    LINE_COLOR = "#ff2d2d"
    AXIS_COLOR = "#444444"
    TEXT_COLOR = "#1f2937"
    ERROR_COLOR = "#b42318"

    SCALE_MIN = 58.0
    SCALE_MAX = 90.0

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

        self.values = [round(self.sensor.reading, 1) for _ in range(self.TOTAL_VALUES)]
        self.message_var = tk.StringVar(
            value=f"Enter a start index from {self.MIN_START_INDEX} to {self.MAX_START_INDEX}."
        )

        self.initUI()
        self.draw_chart(self.MIN_START_INDEX)

    def initUI(self):
        title_label = tk.Label(
            self,
            text="Historical Data",
            font=self.APP_TITLE_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR,
        )
        title_label.pack(pady=(12, 6))

        top_frame = tk.Frame(self, bg=self.WINDOW_BG)
        top_frame.pack(pady=(0, 8))

        input_label = tk.Label(
            top_frame,
            text="Data range:",
            font=self.LABEL_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR,
        )
        input_label.grid(row=0, column=0, padx=(0, 8))

        self.start_entry = tk.Entry(top_frame, width=10, font=self.LABEL_FONT, justify="center")
        self.start_entry.grid(row=0, column=1, padx=(0, 8))
        self.start_entry.insert(0, str(self.MIN_START_INDEX))

        go_button = tk.Button(
            top_frame,
            text="Go",
            width=10,
            font=self.LABEL_FONT,
            command=self.on_go_clicked,
        )
        go_button.grid(row=0, column=2)

        self.range_label = tk.Label(
            self,
            text="",
            font=self.RANGE_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR,
        )
        self.range_label.pack(anchor="w", padx=40, pady=(4, 6))

        self.canvas = tk.Canvas(
            self,
            width=self.CANVAS_WIDTH,
            height=self.CANVAS_HEIGHT,
            bg=self.WINDOW_BG,
            highlightthickness=0,
        )
        self.canvas.pack()

        self.message_label = tk.Label(
            self,
            textvariable=self.message_var,
            font=self.INFO_FONT,
            bg=self.WINDOW_BG,
            fg=self.TEXT_COLOR,
        )
        self.message_label.pack(pady=(6, 0))

    def on_go_clicked(self):
        entry_text = self.start_entry.get().strip()

        if entry_text == "":
            self._set_message("Please enter a start index.", is_error=True)
            return

        if not entry_text.isdigit():
            self._set_message("Start index must be a whole number.", is_error=True)
            return

        start_index = int(entry_text)

        if start_index < self.MIN_START_INDEX or start_index > self.MAX_START_INDEX:
            self._set_message(
                f"Start index must be between {self.MIN_START_INDEX} and {self.MAX_START_INDEX}.",
                is_error=True,
            )
            return

        self.draw_chart(start_index)
        self._set_message("Chart updated successfully.", is_error=False)

    def draw_chart(self, start_index):
        end_index = start_index + self.VISIBLE_VALUES - 1
        visible_data = self.values[start_index:start_index + self.VISIBLE_VALUES]

        self.range_label.config(text=f"Data range: {start_index}-{end_index}")
        self.canvas.delete("all")

        # Uncomment to have greater distance between the bars
        #self.local_min = min(visible_data) - 1
        #self.local_max = max(visible_data) + 1

        chart_width = self.CANVAS_WIDTH - self.CHART_LEFT - self.CHART_RIGHT
        total_gap_width = self.BAR_GAP * (self.VISIBLE_VALUES - 1)
        bar_width = (chart_width - total_gap_width) / self.VISIBLE_VALUES

        self.canvas.create_line(
            self.CHART_LEFT,
            self.CHART_BOTTOM,
            self.CANVAS_WIDTH - self.CHART_RIGHT,
            self.CHART_BOTTOM,
            fill=self.AXIS_COLOR,
            width=2,
        )

        points = []

        for index, value in enumerate(visible_data):
            x1 = self.CHART_LEFT + index * (bar_width + self.BAR_GAP)
            x2 = x1 + bar_width
            y1 = self._value_to_y(value)
            y2 = self.CHART_BOTTOM
            center_x = (x1 + x2) / 2

            self.canvas.create_rectangle(
                x1,
                y1,
                x2,
                y2,
                fill=self.BAR_COLOR,
                outline=self.BAR_OUTLINE,
                width=2,
            )

            self.canvas.create_text(
                center_x,
                y2 + 15,
                text=str(start_index + index),
                font=self.INFO_FONT,
                fill=self.TEXT_COLOR,
            )

            points.extend([center_x, y1])

        self.canvas.create_line(points, fill=self.LINE_COLOR, width=2)

    def _value_to_y(self, value):
        usable_height = self.CHART_BOTTOM - self.CHART_TOP
        normalized_value = (value - self.SCALE_MIN) / (self.SCALE_MAX - self.SCALE_MIN)
        #normalized_value = (value - self.local_min) / (self.local_max - self.local_min)  # Uncomment to have greater distance between the bars
        return self.CHART_BOTTOM - (normalized_value * usable_height)

    def _set_message(self, message, is_error):
        self.message_var.set(message)
        self.message_label.config(fg=self.ERROR_COLOR if is_error else self.TEXT_COLOR)

if __name__ == "__main__":
    app = HumidityHistoricalChart()
    app.mainloop()