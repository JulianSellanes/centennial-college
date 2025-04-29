using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;

//Julian Sellanes (301494667)

namespace comp123
{
    public enum AttendanceStatus
    {
        Present,
        Remote,
        Absent
    }

    public class Student : IComparable<Student>
    {
        public int Id { get; }
        public string Name { get; }
        public string Email { get; }
        public string Grade { get; }
        public AttendanceStatus Attendance { get; }

        public Student(int _id, string _name, string _email, string _grade, AttendanceStatus _attendance)
        {
            Id = _id;
            Name = _name;
            Email = _email;
            Grade = _grade;
            Attendance = _attendance;
        }

        public override bool Equals(object _obj)
        {
            if (_obj is Student other)
                return Id == other.Id;

            return false;
        }

        public override int GetHashCode() => Id.GetHashCode();

        public int CompareTo(Student _other) => Name.CompareTo(_other.Name);

        public override string ToString()
        {
            return $"Id {Id}, Name {Name}, Email {Email}, Grade {Grade}, Attendance {Attendance}";
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Student Id - 301494667, Julian Sellanes");


            //3. Write the logic to read the file “students.csv”
            List<Student> students = new List<Student>();

            try
            {
                var lines = File.ReadAllLines("students.csv").Skip(1);

                foreach (var line in lines)
                {
                    var parts = line.Split(',');

                    if (parts.Length == 5)
                    {
                        int id = int.Parse(parts[0]);
                        string name = parts[1];
                        string email = parts[2];
                        string grade = parts[3];
                        AttendanceStatus attendance = Enum.Parse<AttendanceStatus>(parts[4]);

                        students.Add(new Student(id, name, email, grade, attendance));
                    }
                }
            }
            catch (FileNotFoundException)
            {
                Console.WriteLine("\n====== File not found ======");
            }


            //4. Write LINQ queries for List<student> collection, and print the contents for each of them
            Console.WriteLine("\nAll Students:");
            students.ForEach(Console.WriteLine);

            Console.WriteLine("\nStudents with Grade A:");
            students.Where(s => s.Grade == "A").ToList().ForEach(Console.WriteLine);

            Console.WriteLine("\nNames of Students with Grade B:");
            students.Where(s => s.Grade == "B").Select(s => s.Name).ToList().ForEach(Console.WriteLine);

            Console.WriteLine("\nStudents sorted by Email:");
            students.OrderBy(s => s.Email).ToList().ForEach(Console.WriteLine);

            Console.WriteLine("\nAttendance Groups:");
            var attendanceGroups = students.GroupBy(s => s.Attendance);
            foreach (var group in attendanceGroups)
                Console.WriteLine($"{group.Key}: Count = {group.Count()}");


            //5. Serialize the List of students to a JSON named “students.json”, serialize all properties except “Email”, and “Attendance”
            var options = new JsonSerializerOptions
            {
                WriteIndented = true,
                Encoder = System.Text.Encodings.Web.JavaScriptEncoder.UnsafeRelaxedJsonEscaping
            };
            options.DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull;

            var json = JsonSerializer.Serialize(students.Select(s => new {
                s.Id,
                s.Name,
                s.Grade
            }), options);

            File.WriteAllText("students.json", json);


            //6. Remove all the items from the List of students
            students.Clear();
        }
    }
}