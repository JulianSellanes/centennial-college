using System;
using System.Collections.Generic;
using System.Linq;

//Julian Sellanes (301494667)

namespace Lab23
{
	class Person
	{
		public string Name { get; set; }
		public double Asset { get; set; }
		public bool IsFemale { get; set; }
		public string Country { get; set; }
		public int Age { get; set; }

		public override string ToString()
		{
			return string.Format("{0} {1}B {2} {3} {4}yrs", Name, Asset, IsFemale ? "F" : "M", Country, Age);
		}
	}

	class Program
    {
		static List<Person> persons = new List<Person>()
		{
			new Person(){ Age = 72, Asset = 7.0, Country="South Africa", IsFemale=false, Name="Nicky Oppenheimer"},
			new Person(){ Age = 67, Asset = 7.6, Country="India", IsFemale=true, Name="Savitri Jindal"},
			new Person(){ Age = 81, Asset = 3.1, Country="India", IsFemale=true, Name="Indu Jain"},
			new Person(){ Age = 70, Asset = 2.5, Country="India", IsFemale=true, Name="Vinod Gupta"},
			new Person(){ Age = 77, Asset = 27.0, Country = "US",IsFemale = true,Name = "Jacqueline Mars"},
			new Person(){ Age = 76, Asset = 25.2, Country = "Italy", IsFemale = true, Name = "Maria Franca Fissolo"},
			new Person(){ Age = 55, Asset = 20.4, Country = "Germany", IsFemale = true, Name = "Susanne Klatten"},
			new Person(){ Age = 53, Asset = 20.0, Country = "US",IsFemale = true,Name = "Laurene Jobs"},
			new Person(){ Age = 60, Asset = 12.5, Country = "Nigeria", IsFemale=false, Name="Aliko Dangote" },
			new Person(){ Age = 76, Asset = 10.9, Country = "Ethiopia", IsFemale=false, Name="Mohammed Al Amoudi"},
			new Person(){ Age = 60, Asset = 30.7, Country = "Canada", IsFemale=false, Name="David Thomson" },
			new Person(){ Age = 76, Asset = 11.4, Country = "Canada", IsFemale=false, Name="Galen Weston"},
			new Person(){ Age = 60, Asset = 22.3, Country = "India", IsFemale=false, Name="Mukesh Ambani"},
			new Person(){ Age = 50, Asset = 17.5, Country = "India", IsFemale=false, Name="Dilip Shanghvi"},
			new Person(){ Age = 83, Asset = 30.4, Country = "US", IsFemale=false, Name="Sheldon Adelson"},
			new Person(){ Age = 78, Asset = 30.0, Country = "Brazil", IsFemale=false, Name="Jorge Lemann"},
			new Person(){ Age = 62, Asset = 18.4, Country = "Russia", IsFemale=false, Name="Leonid Mikhelson"},
			new Person(){ Age = 51, Asset = 17.5, Country = "Russia", IsFemale=false, Name="Alexey Mordashov"},
			new Person(){ Age = 89, Asset = 31.2, Country = "Hong Kong", IsFemale=false, Name="Li Ka-shing"},
			new Person(){ Age = 62, Asset = 31.2, Country = "China", IsFemale=false, Name="Wang Jianlin"},
			new Person(){ Age = 67, Asset = 33.8, Country = "US", IsFemale=true, Name="Alice Walton" },
			new Person(){ Age = 60, Asset = 34.0, Country = "US", IsFemale=false, Name="Jim Walton"},
			new Person(){ Age = 72, Asset = 34.1, Country = "US", IsFemale=false, Name="Rob Walton"},
			new Person(){ Age = 94, Asset = 39.5, Country = "France", IsFemale=true, Name="Liliane Bettencourt"},
			new Person(){ Age = 43, Asset = 39.8, Country = "US", IsFemale=false, Name="Sergey Brin"},
			new Person(){ Age = 43, Asset = 39.6, Country = "US", IsFemale=false, Name="Larry Page"},
			new Person(){ Age = 68, Asset = 41.5, Country = "France", IsFemale=false, Name="Bernard Arnault"},
			new Person(){ Age = 75, Asset = 47.5, Country = "US", IsFemale=false, Name="Michael Bloomberg"},
			new Person(){ Age = 77, Asset = 48.3, Country = "US", IsFemale=false, Name="David Koch"},
			new Person(){ Age = 81, Asset = 48.3, Country = "US", IsFemale=false, Name="Charles Koch"},
			new Person(){ Age = 72, Asset = 52.2, Country = "US", IsFemale=false, Name="Larry Ellison"},
			new Person(){ Age = 77, Asset = 54.5, Country = "Mexico", IsFemale=false, Name="Carlos Slim Helu"},
			new Person(){ Age = 33, Asset = 56.0, Country = "US", IsFemale=false, Name="Mark Zuckerberg"},
			new Person(){ Age = 81, Asset = 71.3, Country = "Spain", IsFemale=false, Name="Amancio Ortega"},
			new Person(){ Age = 53, Asset = 72.8, Country = "US", IsFemale=false, Name="Jeff Bezos" },
			new Person(){ Age = 85, Asset = 75.6, Country = "US", IsFemale=false, Name="Warren Buffet" },
			new Person(){ Age = 60, Asset = 86.0, Country = "US", IsFemale=false, Name="Bill Gates"}
		};

		static void Main(string[] args)
        {
			Console.WriteLine($"Student Name : Julián Sellanes");
			Console.WriteLine("\n============== 1 ===============\n");

			//1. Select all the persons with assets of over 50B dollars.
			Query1();
			Console.WriteLine("\n============== 2 ===============\n");

			//2. Select all non-US citizens.
			Query2();
			Console.WriteLine("\n============== 3 ===============\n");

			//3. Select the name of all the females from India. Your query should only capture the person’s name. (This is a projection query)
			Query3();
			Console.WriteLine("\n============== 4 ===============\n");

			//4. Select all persons whose first name is less than five letters long.
			Query4();
			Console.WriteLine("\n============== 5 ===============\n");

			//5. Sort the collection by assets. Your query should only capture the person’s name and asset.
			Query5();
			Console.WriteLine("\n============== 6 ===============\n");

			//6. Group the collection by country.
			Query6();
			Console.WriteLine("\n============== 7 ===============\n");

			//7. Sort the above grouping.
			Query7();
		}

		static void Query1()
        {
			var richPeople = from person in persons
							 where person.Asset > 50
							 select person;

			foreach (var person in richPeople)
				Console.WriteLine(person);
		}

		static void Query2()
		{
			var nonUS = from person in persons
						where person.Country != "US"
						select person;

			foreach (var person in nonUS)
				Console.WriteLine(person);
		}

		static void Query3()
		{
			var indianFemales = from person in persons
								where person.IsFemale && person.Country == "India"
								select person.Name;

			foreach (var name in indianFemales)
				Console.WriteLine(name);
		}

		static void Query4()
		{
			var shortFirstNames = from person in persons
								  let firstName = person.Name.Split(' ')[0]
								  where firstName.Length < 5
								  select person;

			foreach (var person in shortFirstNames)
				Console.WriteLine(person);
		}

		static void Query5()
		{
			var sortedByAssets = from person in persons
								 orderby person.Asset
								 select new { person.Name, person.Asset };

			foreach (var person in sortedByAssets)
				Console.WriteLine($"{person.Name} - {person.Asset}B");
		}

		static void Query6()
		{
			var groupedByCountry = from person in persons
								   group person by person.Country into countryGroup
								   select countryGroup;

			foreach (var group in groupedByCountry)
			{
				Console.WriteLine($"Country: {group.Key}");

				foreach (var person in group)
					Console.WriteLine($"  {person}");
			}
		}

		static void Query7()
		{
			var sortedGroupedByCountry = from person in persons
										 group person by person.Country into countryGroup
										 orderby countryGroup.Key
										 select countryGroup;

			foreach (var group in sortedGroupedByCountry)
			{
				Console.WriteLine($"Country: {group.Key}");

				foreach (var person in group)
					Console.WriteLine($"  {person}");
			}
		}
	}
}