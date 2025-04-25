using System;

//Julian Sellanes (301494667)

namespace Lab18
{
    public class GenericStack<T>
    {
        private const int SIZE = 15;
        private int top;
        private T[] data;

        public bool IsEmpty => top == -1;

        public GenericStack()
        {
            data = new T[SIZE];
            top = -1;
        }

        public void Push(T _item)
        {
            if (top >= SIZE - 1)
                throw new InvalidOperationException("Stack is full");

            data[++top] = _item;
        }

        public T Pop()
        {
            if (IsEmpty)
                throw new InvalidOperationException("Stack is empty");

            return data[top--];
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            TestIntStack();
        }

        static void TestIntStack()
        {
            string studentName = "Julian Sellanes";
            string studentId = "301494667";
            Console.WriteLine($"Executing Lab for Student Name : {studentName}, ID: {studentId}");

            GenericStack<int> stack = new GenericStack<int>();
            Console.WriteLine("Pushing 0 .. 14 in ascending value onto the stack");

            for (int i = 0; i < 15; i++)
            {
                stack.Push(i);
            }

            while (!stack.IsEmpty)
            {
                Console.Write($"{stack.Pop()} ");
            }
            Console.WriteLine();
        }
    }
}