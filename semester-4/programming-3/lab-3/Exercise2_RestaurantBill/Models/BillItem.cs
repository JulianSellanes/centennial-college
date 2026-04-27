using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ComponentModel;

namespace Exercise2_RestaurantBill.Models
{
    public class BillItem : INotifyPropertyChanged
    {
        private int _quantity = 1;

        public string Name { get; set; }
        public string Category { get; set; }
        public decimal Price { get; set; }

        public int Quantity
        {
            get => _quantity;
            set
            {
                int newValue = value < 1 ? 1 : value;

                if (_quantity != newValue)
                {
                    _quantity = newValue;
                    OnPropertyChanged(nameof(Quantity));
                    OnPropertyChanged(nameof(LineTotal));
                }
            }
        }

        public decimal LineTotal => Price * Quantity;

        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}