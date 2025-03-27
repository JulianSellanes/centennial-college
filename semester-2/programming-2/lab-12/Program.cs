using System;

//Julian Sellanes (301494667)

namespace Lab12
{
    class Program
    {
        static void Main(string[] args)
        {
            Complex c0 = new Complex(-2, 3);
            Complex c1 = new Complex(-2, 3);
            Complex c2 = new Complex(1, -2);

            Console.WriteLine($"{c0}");
            Console.WriteLine(c1);
            Console.WriteLine(c2);

            Console.WriteLine($"{c1} + {c2} = {c1 + c2}");
            Console.WriteLine($"{c1} - {c2} = {c1 - c2}");

            Complex c3 = c1 + c2;

            Console.WriteLine($"{c3.Modulus:f2}");

            Console.WriteLine($"{c0} {(c0 == c1 ? "=" : "!=")} {c1}");
            Console.WriteLine($"{c0} {(c0 == c2 ? "=" : "!=")} {c2}");
        }
    }

    public class Complex
    {
        public int Real { get; }
        public int Imaginary { get; }
        public double Modulus
        { 
            get
            {
                return Math.Sqrt(Real * Real + Imaginary * Imaginary);
            }
        }

        public static Complex Zero => new Complex(0, 0);

        public Complex(int _real = 0, int _imaginary = 0)
        {
            this.Real = _real;
            this.Imaginary = _imaginary;
        }

        public override string ToString()
        {
            return $"({Real}, {Imaginary})";
        }

        public static Complex operator +(Complex _lhs, Complex _rhs)
        {
            int real = _lhs.Real + _rhs.Real;
            int imaginary = _lhs.Imaginary + _rhs.Imaginary;

            return new Complex(real, imaginary);
        }

        public static Complex operator -(Complex _lhs, Complex _rhs)
        {
            int real = _lhs.Real - _rhs.Real;
            int imaginary = _lhs.Imaginary - _rhs.Imaginary;

            return new Complex(real, imaginary);
        }

        public static bool operator ==(Complex _lhs, Complex _rhs)
        {
            return _lhs.Real == _rhs.Real && _lhs.Imaginary == _rhs.Imaginary;
        }

        public static bool operator !=(Complex _lhs, Complex _rhs)
        {
            return !(_lhs == _rhs);
        }

        public override bool Equals(object _obj)
        {
            if (_obj is Complex other)
                return this == other;

            return false;
        }

        public override int GetHashCode()
        {
            return (Real, Imaginary).GetHashCode();
        }
    }
}
