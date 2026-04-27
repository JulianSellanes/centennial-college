using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Exercise2_RestaurantBill.Models;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.ComponentModel;

// Julian Sellanes (301494667)

namespace Exercise2_RestaurantBill
{
    public partial class MainWindow : Window
    {
        private const decimal TAX_RATE = 0.13m;

        public ObservableCollection<RestaurantMenuItem> Beverages { get; set; }
        public ObservableCollection<RestaurantMenuItem> Appetizers { get; set; }
        public ObservableCollection<RestaurantMenuItem> MainCourses { get; set; }
        public ObservableCollection<RestaurantMenuItem> Desserts { get; set; }

        public ObservableCollection<BillItem> BillItems { get; set; }

        public MainWindow()
        {
            InitializeComponent();

            LoadMenuData();

            BillItems = new ObservableCollection<BillItem>();
            BillItems.CollectionChanged += BillItems_CollectionChanged;

            DataContext = this;

            UpdateTotals();
        }

        private void LoadMenuData()
        {
            Beverages = new ObservableCollection<RestaurantMenuItem>
            {
                new RestaurantMenuItem { Name = "Soda", Category = "Beverage", Price = 1.95m },
                new RestaurantMenuItem { Name = "Tea", Category = "Beverage", Price = 1.50m },
                new RestaurantMenuItem { Name = "Coffee", Category = "Beverage", Price = 1.25m },
                new RestaurantMenuItem { Name = "Mineral Water", Category = "Beverage", Price = 2.95m },
                new RestaurantMenuItem { Name = "Juice", Category = "Beverage", Price = 2.50m },
                new RestaurantMenuItem { Name = "Milk", Category = "Beverage", Price = 1.50m }
            };

            Appetizers = new ObservableCollection<RestaurantMenuItem>
            {
                new RestaurantMenuItem { Name = "Buffalo Wings", Category = "Appetizer", Price = 5.95m },
                new RestaurantMenuItem { Name = "Buffalo Fingers", Category = "Appetizer", Price = 6.95m },
                new RestaurantMenuItem { Name = "Potato Skins", Category = "Appetizer", Price = 8.95m },
                new RestaurantMenuItem { Name = "Nachos", Category = "Appetizer", Price = 8.95m },
                new RestaurantMenuItem { Name = "Mushroom Caps", Category = "Appetizer", Price = 10.95m },
                new RestaurantMenuItem { Name = "Shrimp Cocktail", Category = "Appetizer", Price = 12.95m },
                new RestaurantMenuItem { Name = "Chips and Salsa", Category = "Appetizer", Price = 6.95m }
            };

            MainCourses = new ObservableCollection<RestaurantMenuItem>
            {
                new RestaurantMenuItem { Name = "Seafood Alfredo", Category = "Main Course", Price = 15.95m },
                new RestaurantMenuItem { Name = "Chicken Alfredo", Category = "Main Course", Price = 13.95m },
                new RestaurantMenuItem { Name = "Chicken Picatta", Category = "Main Course", Price = 13.95m },
                new RestaurantMenuItem { Name = "Turkey Club", Category = "Main Course", Price = 11.95m },
                new RestaurantMenuItem { Name = "Lobster Pie", Category = "Main Course", Price = 19.95m },
                new RestaurantMenuItem { Name = "Prime Rib", Category = "Main Course", Price = 20.95m },
                new RestaurantMenuItem { Name = "Shrimp Scampi", Category = "Main Course", Price = 18.95m },
                new RestaurantMenuItem { Name = "Turkey Dinner", Category = "Main Course", Price = 13.95m },
                new RestaurantMenuItem { Name = "Stuffed Chicken", Category = "Main Course", Price = 14.95m }
            };

            Desserts = new ObservableCollection<RestaurantMenuItem>
            {
                new RestaurantMenuItem { Name = "Apple Pie", Category = "Dessert", Price = 5.95m },
                new RestaurantMenuItem { Name = "Sundae", Category = "Dessert", Price = 3.95m },
                new RestaurantMenuItem { Name = "Carrot Cake", Category = "Dessert", Price = 5.95m },
                new RestaurantMenuItem { Name = "Mud Pie", Category = "Dessert", Price = 4.95m },
                new RestaurantMenuItem { Name = "Apple Crisp", Category = "Dessert", Price = 5.95m }
            };
        }

        private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (sender is ComboBox comboBox && comboBox.SelectedItem is RestaurantMenuItem selectedMenuItem)
            {
                AddOrUpdateBillItem(selectedMenuItem);
                comboBox.SelectedIndex = -1;
            }
        }

        private void AddOrUpdateBillItem(RestaurantMenuItem menuItem)
        {
            BillItem existingItem = BillItems.FirstOrDefault(item => item.Name == menuItem.Name);

            if (existingItem == null)
            {
                BillItems.Add(new BillItem
                {
                    Name = menuItem.Name,
                    Category = menuItem.Category,
                    Price = menuItem.Price,
                    Quantity = 1
                });
            }
            else
            {
                existingItem.Quantity++;
            }
        }

        private void btnRemove_Click(object sender, RoutedEventArgs e)
        {
            if (dgBill.SelectedItem is BillItem selectedItem)
            {
                BillItems.Remove(selectedItem);
            }
            else
            {
                MessageBox.Show("Please select a row to remove.", "Remove Item", MessageBoxButton.OK, MessageBoxImage.Information);
            }
        }

        private void btnClear_Click(object sender, RoutedEventArgs e)
        {
            BillItems.Clear();
            dgBill.SelectedItem = null;
            UpdateTotals();
        }

        private void BillItems_CollectionChanged(object sender, NotifyCollectionChangedEventArgs e)
        {
            if (e.NewItems != null)
            {
                foreach (BillItem item in e.NewItems)
                {
                    item.PropertyChanged += BillItem_PropertyChanged;
                }
            }

            if (e.OldItems != null)
            {
                foreach (BillItem item in e.OldItems)
                {
                    item.PropertyChanged -= BillItem_PropertyChanged;
                }
            }

            UpdateTotals();
        }

        private void BillItem_PropertyChanged(object sender, PropertyChangedEventArgs e)
        {
            UpdateTotals();
        }

        private void UpdateTotals()
        {
            decimal subtotal = BillItems.Sum(item => item.LineTotal);
            decimal tax = subtotal * TAX_RATE;
            decimal total = subtotal + tax;

            txtSubtotal.Text = subtotal.ToString("C");
            txtTax.Text = tax.ToString("C");
            txtTotal.Text = total.ToString("C");
        }

        private void dgBill_CellEditEnding(object sender, DataGridCellEditEndingEventArgs e)
        {
            if (e.Column.DisplayIndex == 3 &&
                e.Row.Item is BillItem billItem &&
                e.EditingElement is TextBox textBox)
            {
                if (!int.TryParse(textBox.Text, out int quantity) || quantity < 1)
                {
                    billItem.Quantity = 1;
                    textBox.Text = "1";
                }
                else
                {
                    billItem.Quantity = quantity;
                }
            }
        }
    }
}
