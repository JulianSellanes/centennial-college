using System;
using System.Collections.Generic;
using System.IO;

//Julian Sellanes (301494667)

namespace Lab9
{
    [Flags]
    public enum SongGenre
    {
        Unclassified = 0,
        Pop = 0b1,
        Rock = 0b10,
        Blues = 0b100,
        Country = 0b1_000,
        Metal = 0b10_000,
        Soul = 0b100_000
    }

    class Program
    {
        static void Main(string[] args)
        {
            //To test the constructor and the ToString method
            Console.WriteLine(new Song("Baby", "Justin Bebier", 3.35, SongGenre.Pop));

            //This is first time that you are using the bitwise or. It is used to specify a combination of genres
            Console.WriteLine(new Song("The Promise", "Chris Cornell", 4.26, SongGenre.Country | SongGenre.Rock));

            Library.LoadSongs("Week_03_lab_09_songs4.txt"); //Class methods are invoke with the class name
            Console.WriteLine("\n\nAll songs");
            Library.DisplaySongs();

            SongGenre genre = SongGenre.Rock;
            Console.WriteLine($"\n\n{genre} songs");
            Library.DisplaySongs(genre);

            string artist = "Bob Dylan";
            Console.WriteLine($"\n\nSongs by {artist}");
            Library.DisplaySongs(artist);

            double length = 5.0;
            Console.WriteLine($"\n\nSongs more than {length}mins");
            Library.DisplaySongs(length);
        }
    }

    public class Song
    {
        public string Title { get; }
        public string Artist { get; }
        public double Length { get; }
        public SongGenre Genre { get; }

        public Song(string _title, string _artist, double _length, SongGenre _genre)
        {
            this.Title = _title;
            this.Artist = _artist;
            this.Length = _length;
            this.Genre = _genre;
        }

        public override string ToString()
        {
             return $"{Title} by {Artist} ({Genre}) {Length}min";
        }
    }

    public static class Library
    {
        private static List<Song> songs = new List<Song>();

        public static void LoadSongs(string _fileName)
        {
            if (!File.Exists(_fileName))
            {
                Console.WriteLine($"File {_fileName} not found.");
                return;
            }

            using (StreamReader reader = new StreamReader(_fileName))
            {
                while (true)
                {
                    string title = reader.ReadLine();
                    if (string.IsNullOrWhiteSpace(title)) break;

                    string artist = reader.ReadLine();
                    double length = Convert.ToDouble(reader.ReadLine());
                    SongGenre genre = Enum.Parse<SongGenre>(reader.ReadLine());

                    songs.Add(new Song(title, artist, length, genre));
                }
            }
        }

        public static void DisplaySongs()
        {
            foreach (Song song in songs)
                Console.WriteLine(song);
        }

        public static void DisplaySongs(double _longerThan)
        {
            foreach (Song song in songs)
                if (song.Length > _longerThan)
                    Console.WriteLine(song);
        }

        public static void DisplaySongs(SongGenre _genre)
        {
            foreach (Song song in songs)
                if (song.Genre != 0 && _genre != 0)
                    Console.WriteLine(song);
        }

        public static void DisplaySongs(string _artist)
        {
            foreach (Song song in songs)
                if (string.Equals(song.Artist, _artist, StringComparison.OrdinalIgnoreCase))
                    Console.WriteLine(song);
        }
    }
}
