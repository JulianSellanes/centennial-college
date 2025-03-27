using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Lab8
{
    enum TimeFormat
    {
        Mil,
        Hour12,
        Hour24
    }

    class Program
    {
        static void Main(string[] args)
        {
            //Create a list to store the objects
            List<Time> times = new List<Time>()
            {
                new Time(9, 35),
                new Time(18, 5),
                new Time(20, 500),
                new Time(10),
                new Time()
            };

            //Display all the objects
            TimeFormat format = TimeFormat.Hour12;
            Console.WriteLine($"\n\nTime format is {format}");
            foreach (Time t in times)
            {
                Console.WriteLine(t);
            }

            //Change the format of the output
            format = TimeFormat.Mil;
            Console.WriteLine($"\n\nSetting time format to {format}");
            Time.SetTimeFormat(format);
            foreach (Time t in times)
            {
                Console.WriteLine(t);
            }

            //Change the format of the output
            format = TimeFormat.Hour24;
            Console.WriteLine($"\n\nSetting time format to {format}");
            Time.SetTimeFormat(format);
            foreach (Time t in times)
            {
                Console.WriteLine(t);
            }
        }
    }

    class Time
    {
        private static TimeFormat TIME_FORMAT = TimeFormat.Hour12;

        public int Hour { get; }
        public int Minute { get; }

        public Time(int _hours = 0, int _minutes = 0)
        {
            if (_hours >= 0 && _hours <= 24)
                this.Hour = _hours;
            else
                this.Hour = 0;

            if (_minutes >= 0 && _minutes <= 59)
                this.Minute = _minutes;
            else
                this.Minute = 0;
        }

        public override string ToString()
        {
            switch (TIME_FORMAT)
            {
                case TimeFormat.Mil:
                    return $"{Hour:D2}{Minute:D2}";

                case TimeFormat.Hour24:
                    return $"{Hour:D2}:{Minute:D2}";

                case TimeFormat.Hour12:
                    int displayHour = Hour % 12;

                    if (displayHour == 0)
                        displayHour = 12;

                    string period = Hour < 12 ? "AM" : "PM";

                    return $"{displayHour}:{Minute:D2} {period}";

                default:
                    return "";
            }
        }

        public static void SetTimeFormat(TimeFormat _timeFormat)
        {
            TIME_FORMAT = _timeFormat;
        }
    }
}
