using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Lab16
{
    class Program
    {
        static void Main(string[] args)
        {
            double length = 2;
            double width = 3;
            List<Shape> shapes = new List<Shape>
            {
                new Square($"square – len:{length}", length),
                new Circle($"circle – rad: {length}", length),
                new Rectangle($"rectangle – wid:{length}, len:{width}", length, width),
                new Triangle($"triangle – bas:{length}, hei:{width}", length, width),
                new Triangle($"triangle – bas:{length *= 2}, hei:{width *= 2}", length, width),
                new Square($"square – len:{length}", length),
                new Circle($"circle – rad: {length}", length),
                new Rectangle($"rectangle – wid:{length}, len:{width}", length, width),
                new Ellipse($"ellipse – min:{length}, maj:{width}", length, width),
                new Diamond($"diamond – min:{length}, maj:{width}", length, width)
            };

            foreach (Shape shape in shapes)
                Console.WriteLine(shape);
        }
    }

    abstract class Shape
    {
        private string name;
        public abstract double Area { get; }

        protected Shape(string _name)
        {
            this.name = _name;
        }

        public override string ToString()
        {
            return $"{name}: {Area:F2}";
        }
    }

    class Square : Shape
    {
        public double Length { get; }
        public override double Area => Length * Length;

        public Square(string _name, double _length) : base(_name)
        {
            this.Length = _length;
        }
    }

    class Circle : Square
    {
        public override double Area => Math.PI * Length * Length;

        public Circle(string _name, double _length) : base(_name, _length) { }
    }

    class Rectangle : Shape
    {
        public double Width { get; }
        public double Length { get; }
        public override double Area => Width * Length;

        public Rectangle(string _name, double _length, double _width) : base(_name)
        {
            this.Length = _length;
            this.Width = _width;
        }
    }

    class Ellipse : Rectangle
    {
        public override double Area => Math.PI * Width * Length;

        public Ellipse(string _name, double _length, double _width) : base(_name, _length, _width) { }
    }

    class Triangle : Rectangle
    {
        public override double Area => 0.5 * Width * Length;

        public Triangle(string _name, double _length, double _width) : base(_name, _length, _width) { }
    }

    class Diamond : Rectangle
    {
        public Diamond(string _name, double _length, double _width) : base(_name, _length, _width) { }
    }
}