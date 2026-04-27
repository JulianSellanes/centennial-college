using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JulianTest
{
    public class Queue4COMP212<T> where T : IEquatable<T>
    {
        private readonly SinglyLinkedList<T> _list;

        public Queue4COMP212()
        {
            _list = new SinglyLinkedList<T>();
        }

        public int Count => _list.GetSize();
        public bool IsEmpty() => _list.IsEmpty();

        public void Enqueue(T item)
        {
            _list.AddLast(item);
        }

        public T Dequeue()
        {
            if (_list.IsEmpty())
                throw new InvalidOperationException("Queue is empty.");

            return _list.RemoveFirst()!;
        }

        public T Peek()
        {
            if (_list.IsEmpty())
                throw new InvalidOperationException("Queue is empty.");

            return _list.First()!;
        }

        public bool Contains(T item)
        {
            var comparer = EqualityComparer<T>.Default;
            foreach (T element in _list.Traverse())
            {
                if (comparer.Equals(element, item))
                    return true;
            }
            return false;
        }

        public T[] ToArray()
        {
            T[] result = new T[_list.GetSize()];
            int i = 0;

            foreach (T element in _list.Traverse())
                result[i++] = element;

            return result;
        }

        public T peek() => Peek();
        public bool contains(T item) => Contains(item);
        public T[] toArray() => ToArray();
    }
}