using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;
using System.IO;
using Microsoft.VisualBasic.FileIO;

//Julian Sellanes (301494667)

namespace Lab1
{
    // Exercise 1:
    public static class ObservableCollectionExtensions
    {
        // 1) Add all elements in a List<T> to the end of the ObservalableCollection<T> object
        public static void AddAll<T>(this ObservableCollection<T> _collection, List<T> _items)
        {
            if (_collection == null) throw new ArgumentNullException(nameof(_collection));
            if (_items == null) throw new ArgumentNullException(nameof(_items));

            foreach (var item in _items)
                _collection.Add(item);
        }

        // 2) Remove all occurrences of items in a List<T> from an ObservalableCollection<T> object
        public static int RemoveAll<T>(this ObservableCollection<T> _collection, List<T> _items)
        {
            if (_collection == null) throw new ArgumentNullException(nameof(_collection));
            if (_items == null) throw new ArgumentNullException(nameof(_items));

            // Remove everything in collection that equals any element in items.
            var toRemove = new HashSet<T>(_items);

            int removedCount = 0;

            // Remove safely from end to start.
            for (int i = _collection.Count - 1; i >= 0; i--)
            {
                if (toRemove.Contains(_collection[i]))
                {
                    _collection.RemoveAt(i);
                    removedCount++;
                }
            }

            return removedCount;
        }
    }

    // Exercise 2:
    public class SinglyLinkedList<T> : IEnumerable<T>
    {
        public class Node<U>
        {
            private U element;
            private Node<U>? next;

            public Node(U e, Node<U>? n = null)
            {
                element = e;
                next = n;
            }

            public U getElement() => element;
            public Node<U>? getNext() => next;
            public void setNext(Node<U>? n) => next = n;
        }

        private Node<T>? head;
        private Node<T>? tail;
        private int size;

        public SinglyLinkedList()
        {
            head = null;
            tail = null;
            size = 0;
        }

        public int getSize() => size;
        public bool isEmpty() => size == 0;

        public T first()
        {
            if (isEmpty()) throw new InvalidOperationException("List is empty.");
            return head!.getElement();
        }

        public T last()
        {
            if (isEmpty()) throw new InvalidOperationException("List is empty.");
            return tail!.getElement();
        }

        public void addFirst(T e)
        {
            var newest = new Node<T>(e, head);
            head = newest;
            if (size == 0) tail = head;
            size++;
        }

        public void addLast(T e)
        {
            var newest = new Node<T>(e, null);

            if (isEmpty())
            {
                head = newest;
                tail = newest;
            }
            else
            {
                tail!.setNext(newest);
                tail = newest;
            }

            size++;
        }

        public T removeFirst()
        {
            if (isEmpty()) throw new InvalidOperationException("List is empty.");

            T answer = head!.getElement();
            head = head.getNext();
            size--;

            if (size == 0) tail = null;

            return answer;
        }

        public override string ToString()
        {
            var sb = new StringBuilder();
            sb.Append("SinglyLinkedList (size = ").Append(size).Append(") [");

            bool firstItem = true;
            foreach (var item in this)
            {
                if (!firstItem) sb.Append(" -> ");
                sb.Append(item?.ToString());
                firstItem = false;
            }

            sb.Append("]");

            return sb.ToString();
        }

        public IEnumerator<T> GetEnumerator()
        {
            var walk = head;
            while (walk != null)
            {
                yield return walk.getElement();
                walk = walk.getNext();
            }
        }

        IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
    }

    public class Medal
    {
        public string Athlete { get; }
        public int Year { get; }
        public int Gold { get; }
        public int Silver { get; }
        public int Bronze { get; }

        public int Total => Gold + Silver + Bronze;

        public Medal(string _athlete, int _year, int _gold, int _silver, int _bronze)
        {
            Athlete = _athlete;
            Year = _year;
            Gold = _gold;
            Silver = _silver;
            Bronze = _bronze;
        }

        public override string ToString()
        {
            return $"{Athlete,-30} {Year,4}  {Gold,4} {Silver,6} {Bronze,6} {Total,7}";
        }
    }

    class Program
    {
        private static IEnumerable<Medal> ReadMedals(string _path)
        {
            using var parser = new TextFieldParser(_path);
            parser.SetDelimiters(",", "\t");
            parser.HasFieldsEnclosedInQuotes = true;
            parser.TrimWhiteSpace = true;

            // Skip header row if present
            if (!parser.EndOfData)
            {
                var header = parser.ReadFields();
            }

            while (!parser.EndOfData)
            {
                var fields = parser.ReadFields();
                if (fields == null || fields.Length == 0) continue;

                if (fields.Length < 5)
                    throw new FormatException("Bad row (expected 5 columns): " + string.Join(" | ", fields));

                string athlete = fields[0];
                int year = int.Parse(fields[1]);
                int gold = int.Parse(fields[2]);
                int silver = int.Parse(fields[3]);
                int bronze = int.Parse(fields[4]);

                yield return new Medal(athlete, year, gold, silver, bronze);
            }
        }

        static void Main(string[] args)
        {
            // Exercise 1 test

            Console.WriteLine("=== Exercise 1: ObservableCollection Extensions ===");

            var oc = new ObservableCollection<string> { "A", "B", "C", "B", "D" };
            var addList = new List<string> { "E", "F", "B" };
            var removeList = new List<string> { "B", "D" };

            Console.WriteLine("Initial: " + string.Join(", ", oc));

            oc.AddAll(addList);
            Console.WriteLine("After AddAll (E, F, B): " + string.Join(", ", oc));

            int removed = oc.RemoveAll(removeList);
            Console.WriteLine($"After RemoveAll (B, D): removed = {removed}");
            Console.WriteLine("Final: " + string.Join(", ", oc));



            // Exercise 2 test

            Console.WriteLine();
            Console.WriteLine("=== Exercise 2: SinglyLinkedList + Medals.csv ===");

            string path = Path.Combine(AppContext.BaseDirectory, "Medals.csv");

            if (!File.Exists(path))
            {
                Console.WriteLine($"ERROR: Could not find '{path}'.");
                return;
            }

            var medalsList = new SinglyLinkedList<Medal>();

            foreach (var medal in ReadMedals(path))
                medalsList.addLast(medal);

            Console.WriteLine();
            Console.WriteLine($"Loaded {medalsList.getSize()} rows:");
            Console.WriteLine("Athlete                       Year    Gold  Silver  Bronze  Total");
            Console.WriteLine("--------------------------------------------------------------------");



            foreach (var m in medalsList)
                Console.WriteLine(m);

            // Totals per athlete
            var totalsByAthlete = new Dictionary<string, int>(StringComparer.OrdinalIgnoreCase);
            foreach (var m in medalsList)
            {
                totalsByAthlete[m.Athlete] = totalsByAthlete.TryGetValue(m.Athlete, out var cur)
                    ? cur + m.Total
                    : m.Total;
            }

            Console.WriteLine();
            Console.WriteLine("Total medals per athlete:");
            Console.WriteLine("----------------------------------");
            foreach (var kv in totalsByAthlete)
                Console.WriteLine($"{kv.Key,-30} {kv.Value}");
        }
    }
}
