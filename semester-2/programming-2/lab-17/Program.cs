using System;
using System.Collections.Generic;

//Julian Sellanes (301494667)

namespace Lab17
{
    class Program
    {
        static void Main(string[] args)
        {
            List<IDrawable> drawables = new List<IDrawable>();
            drawables.Add(new DrawableRectangle("red"));
            drawables.Add(new DrawableEllipse("yellow"));
            drawables.Add(new DrawableBezier("blue"));
            drawables.Add(new DrawableArc("white"));
            drawables.Add(new DrawableLine("black"));

            foreach (IDrawable drawable in drawables)
            {
                drawable.Draw();
                if (drawable is IWritable)
                {
                    IWritable writable = drawable as IWritable;
                    writable.Write();
                }
            }
        }
    }

    public interface IDrawable
    {
        void Draw();
    }

    public interface IWritable
    {
        void Write();
    }

    public abstract class Primitive
    {
        protected string color;

        public Primitive(string _color)
        {
            this.color = _color;
        }
    }

    public class DrawableRectangle : Primitive, IDrawable, IWritable
    {
        public DrawableRectangle(string _color) : base(_color) { }

        public void Draw()
        {
            Console.WriteLine($"Drawing a {color} Rectangle");
        }

        public void Write()
        {
            Console.WriteLine($"Saving a {color} Rectangle to a file");
        }
    }

    public class DrawableEllipse : Primitive, IDrawable
    {
        public DrawableEllipse(string _color) : base(_color) { }

        public void Draw()
        {
            Console.WriteLine($"Drawing a {color} Ellipse");
        }
    }

    public class DrawableLine : IDrawable, IWritable
    {
        protected string color;

        public DrawableLine(string _color)
        {
            this.color = _color;
        }

        public void Draw()
        {
            Console.WriteLine($"Drawing a {color} Line");
        }

        public void Write()
        {
            Console.WriteLine($"Saving a {color} Line to a file");
        }
    }

    public class DrawableBezier : IDrawable, IWritable
    {
        protected string color;

        public DrawableBezier(string _color)
        {
            this.color = _color;
        }

        public void Draw()
        {
            Console.WriteLine($"Drawing a {color} Bezier");
        }

        public void Write()
        {
            Console.WriteLine($"Saving a {color} Bezier to a file");
        }
    }

    public class DrawableArc : Primitive, IDrawable, IWritable
    {
        public DrawableArc(string _color) : base(_color) { }

        public void Draw()
        {
            Console.WriteLine($"Drawing a {color} Arc");
        }

        public void Write()
        {
            Console.WriteLine($"Saving a {color} Arc to a file");
        }
    }
}