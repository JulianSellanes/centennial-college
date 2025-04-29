using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Lab21
{
    class Customer : IComparable<Customer>
    {
        public int Id { get; }
        public string Name { get; }

        public Customer(int _id, string _name)
        {
            this.Id = _id;
            this.Name = _name;
        }

        public override bool Equals(object? _obj)
        {
            return _obj is Customer customer && Id == customer.Id;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(Id);
        }

        public int CompareTo(Customer? _other)
        {
            if (_other == null) return 1;
            return this.Name.CompareTo(_other.Name);
        }

        public override string ToString()
        {
            return $"Id : {Id}, Name : {Name}";
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Customer customer1 = new Customer(1, "ABC");
            Customer customer2 = new Customer(2, "AAA");
            Customer customer3 = new Customer(2, "AAA");
            Customer customer4 = new Customer(4, "CCC");
            Customer customer5 = new Customer(5, "BBB");

            List<Customer> customersList = new List<Customer>() {customer1, customer2, customer3, customer4, customer5};
            HashSet<Customer> customersHashSet = new HashSet<Customer>() {customer1, customer2, customer3, customer4, customer5};
            SortedSet<Customer> customersSortedSet = new SortedSet<Customer>() {customer1, customer2, customer3, customer4, customer5};

            Console.WriteLine($"Student Name : Julián Sellanes");

            Console.WriteLine($"Displaying contents of List");
            foreach (Customer customer in customersList)
            {
                Console.WriteLine(customer);
            }

            Console.WriteLine($"Displaying contents of HashSet");
            foreach (Customer customer in customersHashSet)
            {
                Console.WriteLine(customer);
            }

            Console.WriteLine($"Displaying contents of SortedSet");
            foreach (Customer customer in customersSortedSet)
            {
                Console.WriteLine(customer);
            }
        }
    }
}