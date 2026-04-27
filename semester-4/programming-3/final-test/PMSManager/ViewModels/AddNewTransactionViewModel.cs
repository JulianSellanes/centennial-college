using PMSManager.Command;
using PMSManager.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace PMSManager.ViewModels
{
    public class AddNewTransactionViewModel : ViewModelBase
    {
        private ObservableCollection<SalesPersonComboBoxModel> _salespersons = new ObservableCollection<SalesPersonComboBoxModel>();
        public ObservableCollection<SalesPersonComboBoxModel> Salespersons
        {
            get { return _salespersons; }
            set
            {
                _salespersons = value;
                OnPropertyChanged(nameof(Salespersons));
            }
        }

        private SalesPersonComboBoxModel? _selectedSalesperson;
        public SalesPersonComboBoxModel? SelectedSalesperson
        {
            get { return _selectedSalesperson; }
            set
            {
                _selectedSalesperson = value;
                OnPropertyChanged(nameof(SelectedSalesperson));
            }
        }

        private ObservableCollection<ProductComboBoxModel> _products = new ObservableCollection<ProductComboBoxModel>();
        public ObservableCollection<ProductComboBoxModel> Products
        {
            get { return _products; }
            set
            {
                _products = value;
                OnPropertyChanged(nameof(Products));
            }
        }

        private ProductComboBoxModel? _selectedProduct;
        public ProductComboBoxModel? SelectedProduct
        {
            get { return _selectedProduct; }
            set
            {
                _selectedProduct = value;
                OnPropertyChanged(nameof(SelectedProduct));
            }
        }

        private int _amount;
        public int Amount
        {
            get { return _amount; }
            set
            {
                _amount = value;
                OnPropertyChanged(nameof(Amount));
            }
        }

        private DateTime _transactionDate;
        public DateTime TransactionDate
        {
            get { return _transactionDate; }
            set
            {
                _transactionDate = value;
                OnPropertyChanged(nameof(TransactionDate));
            }
        }

        public DelegateCommand AddNewItemCommand { get; set; }
        public DelegateCommand CancelCommand { get; set; }


        public AddNewTransactionViewModel()
        {
            AddNewItemCommand = new DelegateCommand(AddNewItem);
            CancelCommand = new DelegateCommand(Clear);

            _ = LoadComboBoxesAsync();
        }

        private async Task LoadComboBoxesAsync()
        {
            Salespersons = await dbManager.GetSalespersonsAsync();
            Products = await dbManager.GetProductsAsync();
        }

        private async void AddNewItem()
        {
            if (SelectedSalesperson == null)
            {
                MessageBox.Show("Please select a salesperson.");
                return;
            }

            if (SelectedProduct == null)
            {
                MessageBox.Show("Please select a product.");
                return;
            }

            if (Amount <= 0)
            {
                MessageBox.Show("Amount must be greater than zero.");
                return;
            }

            if (TransactionDate == default)
            {
                MessageBox.Show("Please pick a transaction date.");
                return;
            }

            try
            {
                await dbManager.AddTransactionAsync(
                    SelectedSalesperson.Id,
                    SelectedProduct.Code,
                    Amount,
                    TransactionDate);

                MessageBox.Show("Transaction saved.");
                Clear();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Could not save transaction: " + ex.Message);
            }
        }

        private void Clear()
        {
            SelectedSalesperson = null;
            SelectedProduct = null;
            Amount = 0;
            TransactionDate = default;
        }
    }
}
