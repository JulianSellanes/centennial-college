using System;
using System.IO;
using System.Threading.Tasks;

// Julian Sellanes (301494667)

namespace JulianTest
{
    class Program
    {
        static void Main(string[] args)
        {
            MainAsync(args).GetAwaiter().GetResult();
        }

        private static async Task MainAsync(string[] args)
        {
            string csvPath = args.Length > 0
                ? args[0]
                : Path.Combine(AppContext.BaseDirectory, "Sessions.csv");

            CourseSession[] sessions = await CourseSession.LoadFromCsvAsync(csvPath);

            var q = new Queue4COMP212<CourseSession>();
            foreach (var s in sessions)
                q.Enqueue(s);

            CourseSession[] copy = q.ToArray();

            Console.WriteLine($"Loaded {q.Count} sessions:\n");
            foreach (var s in copy)
                Console.WriteLine(s);
        }
    }
}
