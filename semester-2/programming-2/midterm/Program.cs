using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Midterm
{
    public enum VehicleType
    {
        Car,
        Motorcycle
    }

    public interface IVehicle
    {
        void RentVehicle();
        void ReturnVehicle();
        VehicleType GetVehicleType();
    }

    public abstract class Vehicle : IVehicle
    {
        public string Make { get; }
        public string Model { get; }
        public bool IsRented { get; private set; }

        protected Vehicle(string _make, string _model)
        {
            Make = _make;
            Model = _model;
            IsRented = false;
        }

        public void RentVehicle()
        {
            if (IsRented)
                Console.WriteLine($"Vehicle with Make - {Make}, Model {Model} is not available");
            else
                IsRented = true;
        }

        public void ReturnVehicle()
        {
            if (!IsRented)
                Console.WriteLine($"Vehicle with Make - {Make}, Model {Model} is not rented");
            else
                IsRented = false;
        }

        public abstract VehicleType GetVehicleType();

        public override string ToString()
        {
            return $"Make - {Make}, Model - {Model}, IsRented - {IsRented}, Type - {GetVehicleType()}";
        }
    }

    public class Car : Vehicle
    {
        public Car(string _make, string _model) : base(_make, _model) { }
        public override VehicleType GetVehicleType() => VehicleType.Car;
    }

    public class Motorcycle : Vehicle
    {
        public Motorcycle(string _make, string _model) : base(_make, _model) { }
        public override VehicleType GetVehicleType() => VehicleType.Motorcycle;
    }

    class Program
    {
        static void Main(string[] args)
        {
            Vehicle vehicle1 = new Car("Honda", "CRV");
            Vehicle vehicle2 = new Car("Honda", "Civic");
            Vehicle vehicle3 = new Car("Toyota", "Camry");
            Vehicle vehicle4 = new Motorcycle("Harley Davidson", "Street Glide");
            Vehicle vehicle5 = new Motorcycle("Ducati", "Multistrada");

            vehicle1.ReturnVehicle();
            vehicle1.RentVehicle();
            vehicle1.ReturnVehicle();

            vehicle2.RentVehicle();
            vehicle2.RentVehicle();
            vehicle2.ReturnVehicle();

            List<Vehicle> vehicles = new List<Vehicle>() { vehicle1, vehicle2, vehicle3, vehicle4, vehicle5 };

            Console.WriteLine("---- Displaying all the Motorcycles ----");
            foreach (Vehicle vehicle in vehicles)
                if (vehicle.GetVehicleType() == VehicleType.Motorcycle)
                    Console.WriteLine(vehicle);
        }
    }
}
