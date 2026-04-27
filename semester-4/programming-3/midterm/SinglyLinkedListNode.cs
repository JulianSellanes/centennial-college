using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JulianTest
{
    internal sealed class SinglyLinkedListNode<T>
    {
        internal T element;            // reference to the element stored at this node

        internal SinglyLinkedListNode<T> next;         // reference to the subsequent node in the list

        internal SinglyLinkedListNode(T e, SinglyLinkedListNode<T> n)
        {
            element = e;
            next = n;
        }

        internal T GetElement() { return element; }

        internal SinglyLinkedListNode<T> GetNext() { return next; }

        internal void SetNext(SinglyLinkedListNode<T>? n) { next = n!; }
    }
}