using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace JulianTest
{
    public sealed class CourseSession : IEquatable<CourseSession>
    {
        public string Id { get; }
        public string Semester { get; }
        public string CourseTitle { get; }
        public int Enrollment { get; }

        public CourseSession(string id, string semester, string courseTitle, int enrollment)
        {
            Id = id ?? throw new ArgumentNullException(nameof(id));
            Semester = semester ?? throw new ArgumentNullException(nameof(semester));
            CourseTitle = courseTitle ?? throw new ArgumentNullException(nameof(courseTitle));
            Enrollment = enrollment;
        }

        // Equality key: Id + Semester
        public bool Equals(CourseSession? other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;

            return string.Equals(Id, other.Id, StringComparison.OrdinalIgnoreCase)
                && string.Equals(Semester, other.Semester, StringComparison.OrdinalIgnoreCase);
        }

        public override bool Equals(object? obj) => Equals(obj as CourseSession);

        public override int GetHashCode()
        {
            unchecked
            {
                int hash = 17;
                hash = (hash * 23) + StringComparer.OrdinalIgnoreCase.GetHashCode(Id);
                hash = (hash * 23) + StringComparer.OrdinalIgnoreCase.GetHashCode(Semester);
                return hash;
            }
        }

        public override string ToString()
            => $"{Id,-12}  {Semester,-10}  {CourseTitle,-40}  Enrollment: {Enrollment}";

        // Async CSV loader
        public static async Task<CourseSession[]> LoadFromCsvAsync(string csvPath)
        {
            if (string.IsNullOrWhiteSpace(csvPath))
                throw new ArgumentException("CSV path is required.", nameof(csvPath));

            if (!File.Exists(csvPath))
                throw new FileNotFoundException("CSV file not found.", csvPath);

            var sessions = new System.Collections.Generic.List<CourseSession>();

            using (var reader = new StreamReader(csvPath))
            {
                // Skip header
                await reader.ReadLineAsync();

                string? line;
                while ((line = await reader.ReadLineAsync()) != null)
                {
                    if (string.IsNullOrWhiteSpace(line))
                        continue;

                    string[] parts = line.Split(',');
                    if (parts.Length < 4)
                        continue;

                    string id = parts[0].Trim();
                    string semester = parts[1].Trim();
                    string title = parts[2].Trim();

                    if (!int.TryParse(parts[3].Trim(), out int enrollment))
                        enrollment = 0;

                    sessions.Add(new CourseSession(id, semester, title, enrollment));
                }
            }

            return sessions.ToArray();
        }
    }
}