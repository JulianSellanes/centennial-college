using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Concurrent;
using System.Globalization;
using System.IO;

// Julian Sellanes (301494667)

namespace Exercise1_FileSearch
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== Parallel File Search ===");
            Console.Write("Enter root path (example: C:\\): ");
            string rootPath = Console.ReadLine();

            if (string.IsNullOrWhiteSpace(rootPath) || !Directory.Exists(rootPath))
            {
                Console.WriteLine("Invalid root path.");
                return;
            }

            Console.Write("Enter search pattern (example: *.txt): ");
            string searchPattern = Console.ReadLine();

            if (string.IsNullOrWhiteSpace(searchPattern))
            {
                Console.WriteLine("Invalid search pattern.");
                return;
            }

            ConcurrentBag<FileReport> results = new ConcurrentBag<FileReport>();

            Console.WriteLine();
            Console.WriteLine("Searching, please wait...");
            Console.WriteLine();

            SearchDirectoryParallel(rootPath, searchPattern, results);

            List<FileReport> sortedResults = results
                .OrderBy(r => r.FullPath)
                .ToList();

            Console.WriteLine("Search completed.");
            Console.WriteLine("Total matches found: " + sortedResults.Count);
            Console.WriteLine();

            foreach (FileReport result in sortedResults)
            {
                Console.WriteLine(result.FullPath);
            }

            string csvPath = Path.Combine(Environment.CurrentDirectory, "search_results.csv");
            SaveResultsToCsv(csvPath, sortedResults);

            Console.WriteLine();
            Console.WriteLine("Results saved to: " + csvPath);
            Console.WriteLine("Press any key to exit...");
            Console.ReadKey();
        }

        static void SearchDirectoryParallel(string currentDirectory, string searchPattern, ConcurrentBag<FileReport> results)
        {
            try
            {
                string[] files = Directory.GetFiles(currentDirectory, searchPattern, SearchOption.TopDirectoryOnly);

                foreach (string filePath in files)
                {
                    try
                    {
                        FileInfo fileInfo = new FileInfo(filePath);

                        results.Add(new FileReport
                        {
                            FileName = fileInfo.Name,
                            FullPath = fileInfo.FullName,
                            SizeKB = Math.Round(fileInfo.Length / 1024.0, 2),
                            LastModified = fileInfo.LastWriteTime.ToString("yyyy-MM-dd HH:mm:ss")
                        });
                    }
                    catch (Exception)
                    {
                        // Ignore files that cannot be read
                    }
                }

                string[] subDirectories = Directory.GetDirectories(currentDirectory, "*", SearchOption.TopDirectoryOnly);

                Parallel.ForEach(subDirectories, subDirectory =>
                {
                    SearchDirectoryParallel(subDirectory, searchPattern, results);
                });
            }
            catch (UnauthorizedAccessException)
            {
                // Skip folders without permission
            }
            catch (PathTooLongException)
            {
                // Skip paths that are too long
            }
            catch (DirectoryNotFoundException)
            {
                // Skip deleted/missing folders
            }
            catch (IOException)
            {
                // Skip folders causing I/O issues
            }
            catch (Exception)
            {
                // Skip any other unexpected folder errors
            }
        }

        static void SaveResultsToCsv(string filePath, List<FileReport> results)
        {
            using (StreamWriter writer = new StreamWriter(filePath))
            {
                writer.WriteLine("File Name,Full Path,Size (KB),Last Modified");

                foreach (FileReport result in results)
                {
                    writer.WriteLine(
                        EscapeCsv(result.FileName) + "," +
                        EscapeCsv(result.FullPath) + "," +
                        result.SizeKB.ToString("F2", CultureInfo.InvariantCulture) + "," +
                        EscapeCsv(result.LastModified)
                    );
                }
            }
        }

        static string EscapeCsv(string value)
        {
            if (value == null)
                return "";

            if (value.Contains(",") || value.Contains("\"") || value.Contains("\n"))
            {
                value = value.Replace("\"", "\"\"");
                return "\"" + value + "\"";
            }

            return value;
        }
    }
}
