using PMSManager.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PMSManager.ViewModels
{
    public class ListEditAllTransactionsViewModel : ViewModelBase
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

                if (_selectedSalesperson != null)
                {
                    _ = LoadTransactionsAsync(_selectedSalesperson.Id);
                }
            }
        }

        private ObservableCollection<SalesDetails> _transactions = new ObservableCollection<SalesDetails>();
        public ObservableCollection<SalesDetails> Transactions
        {
            get { return _transactions; }
            set
            {
                _transactions = value;
                OnPropertyChanged(nameof(Transactions));
            }
        }

        public ListEditAllTransactionsViewModel()
        {
            _ = LoadSalespersonsAsync();
        }

        private async Task LoadSalespersonsAsync()
        {
            Salespersons = await dbManager.GetSalespersonsAsync();
        }

        private async Task LoadTransactionsAsync(string employeeId)
        {
            Transactions = await dbManager.GetTransactionsByEmployeeAsync(employeeId);
        }
    }
}
