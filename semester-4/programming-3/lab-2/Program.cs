using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

//Julian Sellanes (301494667)

namespace Lab2
{
    // Exercise 1:

    // DoublyLinkedList<TKey, TValue>
    public class DoublyLinkedList<TKey, TValue>
    {
        // Fields
        private DoublyLinkedListNode head;
        private DoublyLinkedListNode tail;

        // Property
        public int Size { get; private set; }

        // Nested Type
        public class DoublyLinkedListNode
        {
            // Fields
            private TKey Key;
            private TValue Value;
            private DoublyLinkedListNode Next;
            private DoublyLinkedListNode Prev;

            // Methods
            public DoublyLinkedListNode() { }

            public DoublyLinkedListNode(TKey key, TValue value, DoublyLinkedListNode prev = null, DoublyLinkedListNode next = null)
            {
                Key = key;
                Value = value;
                Prev = prev;
                Next = next;
            }

            public TKey getKey() => Key;
            public TValue getValue() => Value;
            public DoublyLinkedListNode getNext() => Next;
            public DoublyLinkedListNode getPrev() => Prev;

            public void setKey(TKey key) => Key = key;
            public void setValue(TValue value) => Value = value;
            public void setNext(DoublyLinkedListNode next) => Next = next;
            public void setPrev(DoublyLinkedListNode prev) => Prev = prev;
        }

        // Constructors
        public DoublyLinkedList()
        {
            head = null;
            tail = null;
            Size = 0;
        }

        public DoublyLinkedList(TKey key, TValue value) : this()
        {
            AddLast(key, value);
        }

        // Methods
        public void AddFirst(TKey key, TValue value)
        {
            var node = new DoublyLinkedListNode(key, value, prev: null, next: head);

            if (IsEmpty())
            {
                head = tail = node;
            }
            else
            {
                head.setPrev(node);
                head = node;
            }

            Size++;
        }

        public void AddLast(TKey key, TValue value)
        {
            var node = new DoublyLinkedListNode(key, value, prev: tail, next: null);

            if (IsEmpty())
            {
                head = tail = node;
            }
            else
            {
                tail.setNext(node);
                tail = node;
            }

            Size++;
        }

        public DoublyLinkedListNode First() => head;

        public DoublyLinkedListNode Last() => tail;

        public List<TKey> getKeys()
        {
            var keys = new List<TKey>();
            var cur = head;
            while (cur != null)
            {
                keys.Add(cur.getKey());
                cur = cur.getNext();
            }
            return keys;
        }

        public List<TValue> getValues()
        {
            var values = new List<TValue>();
            var cur = head;
            while (cur != null)
            {
                values.Add(cur.getValue());
                cur = cur.getNext();
            }
            return values;
        }

        public bool IsEmpty() => Size == 0;

        public bool Remove(TKey key)
        {
            var node = FindNode(key);
            if (node == null) return false;

            RemoveNode(node);
            return true;
        }

        // Helpers
        internal DoublyLinkedListNode FindNode(TKey key)
        {
            var comparer = EqualityComparer<TKey>.Default;

            var cur = head;
            while (cur != null)
            {
                if (comparer.Equals(cur.getKey(), key))
                    return cur;

                cur = cur.getNext();
            }
            return null;
        }

        internal void RemoveNode(DoublyLinkedListNode node)
        {
            var prev = node.getPrev();
            var next = node.getNext();

            if (prev != null) prev.setNext(next);
            else head = next;

            if (next != null) next.setPrev(prev);
            else tail = prev;

            node.setPrev(null);
            node.setNext(null);

            Size--;
        }
    }

    // Map_DoublyLinkedList<TKey, TValue>
    public class Map_DoublyLinkedList<TKey, TValue>
    {
        // Field
        private readonly DoublyLinkedList<TKey, TValue> map = new DoublyLinkedList<TKey, TValue>();

        public int Size => map.Size;

        // Methods
        public void Put(TKey key, TValue value)
        {
            if (object.ReferenceEquals(key, null)) throw new ArgumentNullException(nameof(key));

            var node = map.FindNode(key);
            if (node != null)
            {
                node.setValue(value);
            }
            else
            {
                map.AddLast(key, value);
            }
        }

        public TValue Get(TKey key)
        {
            if (object.ReferenceEquals(key, null)) throw new ArgumentNullException(nameof(key));

            var node = map.FindNode(key);
            if (node == null)
                throw new KeyNotFoundException($"Key '{key}' was not found.");

            return node.getValue();
        }

        public bool Remove(TKey key)
        {
            if (object.ReferenceEquals(key, null)) throw new ArgumentNullException(nameof(key));
            return map.Remove(key);
        }

        // Helpers
        public List<TKey> getKeys() => map.getKeys();
        public List<TValue> getValues() => map.getValues();
    }

    // Driver class
    /*
    static class Program
    {
        static void Main()
        {
            var capitals = new Map_DoublyLinkedList<string, string>();
            capitals.Put("USA", "Washington, D.C.");
            capitals.Put("Germany", "Berlin");

            MessageBox.Show($"Germany -> {capitals.Get("Germany")}\nSize: {capitals.Size}");
        }
    }
    */

    // Exercise 2:

    // Driver class
    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }
}
