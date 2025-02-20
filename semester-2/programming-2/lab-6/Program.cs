using System;
using System.Collections.Generic;
using System.IO;

//Julian Sellanes (301494667)

namespace Lab6
{
    class Program
    {
        static void Main(string[] args)
        {
            //Create a medal object
            Medal m1 = new Medal("Horace Gwynne", "Boxing", MedalColor.Gold, 2012, true);
            //Print the object
            Console.WriteLine(m1);
            //Print only the name of the medal holder
            Console.WriteLine(m1.Name);
            //Create another object
            Medal m2 = new Medal("Michael Phelps", "Swimming", MedalColor.Gold, 2012, false);
            //Print the updated m2
            Console.WriteLine(m2);

            //Create a list to store the medal objects
            List<Medal> medals = new List<Medal>() { m1, m2 };
            medals.Add(new Medal("Ryan Cochrane", "Swimming", MedalColor.Silver, 2012, false));
            medals.Add(new Medal("Adam van Koeverden", "Canoeing", MedalColor.Silver, 2012, false));
            medals.Add(new Medal("Rosie MacLennan", "Gymnastics", MedalColor.Gold, 2012, false));
            medals.Add(new Medal("Christine Girard", "Weightlifting", MedalColor.Bronze, 2012, false));
            medals.Add(new Medal("Charles Hamelin", "Short Track", MedalColor.Gold, 2014, true));
            medals.Add(new Medal("Alexandre Bilodeau", "Freestyle skiing", MedalColor.Gold, 2012, true));
            medals.Add(new Medal("Jennifer Jones", "Curling", MedalColor.Gold, 2014, false));
            medals.Add(new Medal("Charle Cournoyer", "Short Track", MedalColor.Bronze, 2014, false));
            medals.Add(new Medal("Mark McMorris", "Snowboarding", MedalColor.Bronze, 2014, false));
            medals.Add(new Medal("Sidney Crosby ", "Ice Hockey", MedalColor.Gold, 2014, false));
            medals.Add(new Medal("Brad Jacobs", "Curling", MedalColor.Gold, 2014, false));
            medals.Add(new Medal("Ryan Fry", "Curling", MedalColor.Gold, 2014, false));
            medals.Add(new Medal("Antoine Valois-Fortier", "Judo", MedalColor.Bronze, 2012, false));
            medals.Add(new Medal("Brent Hayden", "Swimming", MedalColor.Bronze, 2012, false));


            //Prints a numbered list of 16 medals.
            Console.WriteLine("\n\nAll 16 medals");
            for (int i = 0; i < medals.Count; i++)
                Console.WriteLine($"{i + 1}. {medals[i]}");


            //Prints a numbered list of 16 names (ONLY)
            Console.WriteLine("\n\nAll 16 names");
            for (int i = 0; i < medals.Count; i++)
                Console.WriteLine($"{i + 1}. {medals[i].Name}");


            //Prints a numbered list of 9 gold medals
            Console.WriteLine("\n\nAll 9 gold medals");

            List<Medal> goldMedals = new List<Medal>();
            foreach (Medal m in medals)
                if (m.Color == MedalColor.Gold)
                    goldMedals.Add(m);

            for (int i = 0; i < goldMedals.Count; i++)
                Console.WriteLine($"{i + 1}. {goldMedals[i]}");


            //Prints a numbered list of 9 medals in 2012
            Console.WriteLine("\n\nAll 9 medals in 2012");

            List<Medal> medals2012 = new List<Medal>();
            foreach (Medal m in medals)
                if (m.Year == 2012)
                    medals2012.Add(m);

            for (int i = 0; i < medals2012.Count; i++)
                Console.WriteLine($"{i + 1}. {medals2012[i]}");


            //Prints a numbered list of 4 gold medals in 2012
            Console.WriteLine("\n\nAll 4 gold medals");

            List<Medal> goldMedals2012 = new List<Medal>();
            foreach (Medal m in medals)
                if (m.Color == MedalColor.Gold && m.Year == 2012)
                    goldMedals2012.Add(m);

            for (int i = 0; i < goldMedals2012.Count; i++)
                Console.WriteLine($"{i + 1}. {goldMedals2012[i]}");


            //Prints a numbered list of 3 world record medals
            Console.WriteLine("\n\nAll 3 records");

            List<Medal> recordMedals = new List<Medal>();
            foreach (Medal m in medals)
                if (m.IsRecord)
                    recordMedals.Add(m);

            for (int i = 0; i < recordMedals.Count; i++)
                Console.WriteLine($"{i + 1}. {recordMedals[i]}");


            //Saving all the medal to file Medals.txt
            Console.WriteLine("\n\nSaving to file");

            using (StreamWriter writer = new StreamWriter("Medals.txt"))
            {
                foreach (var medal in medals)
                    writer.WriteLine(medal);
            }
        }
    }

    enum MedalColor
    {
        Bronze,
        Silver,
        Gold
    }

    class Medal
    {
        public string Name { get; }
        public string TheEvent { get; }
        public MedalColor Color { get; }
        public int Year { get; }
        public bool IsRecord { get; }

        public Medal(string _name, string _theEvent, MedalColor _color, int _year, bool _isRecord)
        {
            this.Name = _name;
            this.TheEvent = _theEvent;
            this.Color = _color;
            this.Year = _year;
            this.IsRecord = _isRecord;
        }

        public override string ToString()
        {
            string recordIndicator = IsRecord ? "(R)" : "";
            return $"{Year} - {TheEvent}{recordIndicator} {Name}({Color})";
        }
    }
}