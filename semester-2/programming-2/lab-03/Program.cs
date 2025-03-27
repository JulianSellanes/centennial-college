using System;

//Julian Sellanes (301494667)

namespace Lab3
{
    class Program
    {
        static void Main(string[] args)
        {
            Date date1 = new Date(2024, 9, 17);
            Console.WriteLine(date1);

            Console.WriteLine("\nAdd 3 days");
            date1.Add(3);
            Console.WriteLine(date1);

            Console.WriteLine("\nAdd 5 days & 1 month");
            date1.Add(5, 1);
            Console.WriteLine(date1);

            Console.WriteLine("\nAdd 30 days & 9 months & 2000 years");
            Date date2 = new Date(2000, 9, 30);
            date1.Add(date2);
            Console.WriteLine(date1);
        }
    }

    class Date
    {
        private int year;
        private int month;
        private int day;

        public Date(int _year = 2022, int _month = 1, int _day = 1)
        {
            this.year = _year;
            this.month = _month;
            this.day = _day;
        }

        public override string ToString()
        {
            string monthName = $"{this.month}";

            switch (this.month)
            {
                case 1: monthName = "Jan"; break;
                case 2: monthName = "Feb"; break;
                case 3: monthName = "Mar"; break;
                case 4: monthName = "Apr"; break;
                case 5: monthName = "May"; break;
                case 6: monthName = "Jun"; break;
                case 7: monthName = "Jul"; break;
                case 8: monthName = "Aug"; break;
                case 9: monthName = "Sep"; break;
                case 10: monthName = "Oct"; break;
                case 11: monthName = "Nov"; break;
                case 12: monthName = "Dec"; break;
                default: break;
            }

            return $"{monthName}/{day}/{year}";
        }

        public void Add(int _days)
        {
            this.day += _days;
            Normalize();
        }

        public void Add(int _days, int _months)
        {
            this.day += _days;
            this.month += _months;
            Normalize();
        }

        public void Add(Date _date)
        {
            this.day += _date.day;
            this.month += _date.month;
            this.year += _date.year;
            Normalize();
        }

        private void Normalize()
        {
            while (this.day > 30)
            {
                this.day -= 30;
                this.month++;
            }

            while (this.month > 12)
            {
                this.month -= 12;
                this.year++;
            }
        }
    }
}