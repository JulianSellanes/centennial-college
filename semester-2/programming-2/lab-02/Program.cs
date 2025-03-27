using System;

//Julian Sellanes (301494667)

namespace Lab2
{
    class Program
    {
        static void Main(string[] args)
        {
            Car car1 = new Car(2024, "Toyota Camry", 26320);
            Car car2 = new Car(2023, "Ford Mustang", 27770, false);
            Car car3 = new Car(2022, "Honda Civic", 22550);
            Car car4 = new Car(2021, "Chevrolet Silverado", 29300, false);

            Console.WriteLine(car1);
            Console.WriteLine(car2);
            Console.WriteLine(car3);
            Console.WriteLine(car4);
        }
    }

    class Car
    {
        private int year;
        private string model;
        private bool isDrivable;
        private double price;

        public Car (int _year, string _model, double _price, bool _isDrivable = true)
        {
            this.year = _year;
            this.model = _model;
            this.price = _price;
            this.isDrivable = _isDrivable;
        }

        public override string ToString()
        {
            return $"Year: {year}, Model: {model}, Price: ${price}, isDrivable: {isDrivable}";
        }
    }
}