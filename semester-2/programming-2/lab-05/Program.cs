using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Lab5
{
    class Program
    {
        static void Main(string[] args)
        {
            //Create four objects
            Pet pet1 = new Pet("A", 1, "The first one");
            Pet pet2 = new Pet("B", 2, "The second one");
            Pet pet3 = new Pet("C", 3, "The third one");
            Pet pet4 = new Pet("D", 4, "The fourth one");


            //Create a List to store all the above objects
            List<Pet> pets = new List<Pet> { pet1, pet2, pet3, pet4 };


            //Use some of the methods on some of the objects
            pet1.Train();
            pet1.SetOwner("Julian");
            pet2.SetOwner("Desmond");


            //Using a suitable looping statement, display all the objects in the collection
            Console.WriteLine("All pets:");
            foreach (var pet in pets)
            {
                Console.WriteLine(pet);
            }


            //Prompt the user for an owner’s name and then display only the pets belonging to a particular person
            Console.Write("\nEnter an owner's name: ");
            string owner = Console.ReadLine();

            List<string> ownerPets = new List<string>();

            if (pets.Count > 0)
                foreach (Pet pet in pets)
                    if(pet.Owner == owner)
                        ownerPets.Add(pet.Name);

            if (ownerPets.Count > 0)
            {
                Console.WriteLine($"\n{owner}'s pets:");

                foreach (string pet in ownerPets)
                    Console.WriteLine(pet);
            }
            else
            {
                Console.WriteLine($"\nNo pets found");
            }
        }
    }

    class Pet
    {
        public string Name { get; }
        public string Owner { get; private set; }
        public int Age { get; }
        public string Description { get; }
        public bool IsHouseTrained { get; private set; }

        public Pet(string _name, int _age, string _description)
        {
            this.Name = _name;
            this.Age = _age;
            this.Description = _description;
            this.Owner = "no one";
            this.IsHouseTrained = false;
        }

        public override string ToString()
        {
            return $"Name: {Name}, Age: {Age}, Description: {Description}, Owner: {Owner}, IsHouseTrained: {IsHouseTrained}";
        }

        public void Train()
        {
            this.IsHouseTrained = true;
        }

        public void SetOwner(string _newOwner)
        {
            this.Owner = _newOwner;
        }
    }
}
