import random
import matplotlib.pyplot as plt

# Julian Sellanes (301494667)

class SensorDataGenerator:
    # Simulates a sensor that produces readings in Toronto-area campus outdoors in winter
    # Public property: reading -> returns one scaled sensor value
    # Private method: __normalized_value() -> returns a value in the range 0 to 1

    def __init__(
        self,
        minimum=35.0,
        maximum=85.0,
        baseline=0.28,
        spread=0.22,
        min_segment=20,
        max_segment=75,
        drift_factor=0.10,
        wiggle_step=0.012,
        wiggle_limit=0.05,
        jitter=0.004,
        seed=None
    ):
        if minimum >= maximum:
            raise ValueError("minimum must be less than maximum")
        if min_segment < 1 or max_segment < min_segment:
            raise ValueError("segment values are invalid")

        if seed is not None:
            random.seed(seed)

        self._minimum = minimum
        self._maximum = maximum
        self._baseline = baseline
        self._spread = spread
        self._min_segment = min_segment
        self._max_segment = max_segment
        self._drift_factor = drift_factor
        self._wiggle_step = wiggle_step
        self._wiggle_limit = wiggle_limit
        self._jitter = jitter

        self._current = self.__clamp(random.gauss(self._baseline, self._spread / 2))
        self._target = self.__pick_target()
        self._steps_left = random.randint(self._min_segment, self._max_segment)
        self._wiggle = 0.0

    def __clamp(self, value, low=0.0, high=1.0):
        if value < low:
            return low
        if value > high:
            return high
        return value

    def __pick_target(self):
        return self.__clamp(random.gauss(self._baseline, self._spread))

    def __normalized_value(self):
        # Returns a normalized value in the range 0 to 1.
        # This creates:
        # - irregular peaks and valleys
        # - different peak widths
        # - small squiggles/noise

        if self._steps_left <= 0:
            self._target = self.__pick_target()
            self._steps_left = random.randint(self._min_segment, self._max_segment)

        # Slowly move toward the current target
        drift = (self._target - self._current) * self._drift_factor

        # Small smooth squiggles
        self._wiggle = (self._wiggle * 0.75) + random.uniform(-self._wiggle_step, self._wiggle_step)
        self._wiggle = self.__clamp(self._wiggle, -self._wiggle_limit, self._wiggle_limit)

        # Tiny random noise
        noise = random.gauss(0.0, self._jitter)

        self._current = self.__clamp(self._current + drift + self._wiggle + noise)
        self._steps_left -= 1

        return self._current

    @property
    def reading(self):
        x = self.__normalized_value()
        m = self._maximum - self._minimum
        c = self._minimum
        y = m * x + c
        return y

def x_time(number_of_values, value, unit):
    unit = unit.lower()

    x_values = [i * value for i in range(number_of_values)]

    if unit == "minute" or unit == "minutes":
        xlabel = "Time (minutes)"
    elif unit == "hour" or unit == "hours":
        xlabel = "Time (hours)"
    elif unit == "day" or unit == "days":
        xlabel = "Time (days)"
    else:
        xlabel = f"Time ({unit})"

    return x_values, xlabel

def main():
    '''
    After some research, for a Toronto-area campus outdoors in winter, a realistic relative humidity pattern is roughly this:
    - Normal operating range: 60% to 90%
    - Typical average center: around 74%
    - Occasional drier dips: high 50s
    - Humid spikes: high 80s to low 90s
    '''
    sensor = SensorDataGenerator(
        minimum=58.0,
        maximum=90.0,
        baseline=0.50,
        spread=0.18,
        min_segment=28,
        max_segment=42,
        drift_factor=0.08,
        wiggle_step=0.007,
        wiggle_limit=0.03,
        jitter=0.002
    )

    # With 500 readings, 20 minutes = ~7 days
    number_of_values = 500
    x_values, x_label = x_time(number_of_values, 20.2, "minutes")
    y_values = [sensor.reading for _ in range(number_of_values)]

    plt.figure(figsize=(12, 4))
    plt.plot(x_values, y_values, linewidth=1.8)
    plt.title("Outdoor Humidity Near a Toronto Campus in Winter")
    plt.xlabel(x_label)
    plt.ylabel("Humidity (%)")
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    main()