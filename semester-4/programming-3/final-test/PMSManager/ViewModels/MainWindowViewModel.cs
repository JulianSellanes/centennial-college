using PMSManager.Command;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace PMSManager.ViewModels
{
    public class MainWindowViewModel : ViewModelBase
    {
        private ViewModelBase _CurrentViewModel;

        ListEditAllTransactionsViewModel _listViewModel = new ListEditAllTransactionsViewModel();
        AddNewTransactionViewModel _addViewModel = new AddNewTransactionViewModel();

        public ViewModelBase CurrentViewModel
        {
            get { return _CurrentViewModel; }
            set
            {
                SetProperty(ref _CurrentViewModel, value);
            }
        }

        public DelegateCommand View1Command { get; set; }
        public DelegateCommand View2Command { get; set; }

        public DelegateCommand ExitCommand { get; set; }

        public MainWindowViewModel()
        {
            View1Command = new DelegateCommand(ShowListview);
            View2Command = new DelegateCommand(ShowAddView);
            ExitCommand = new DelegateCommand(ExitApp);

            CurrentViewModel = _listViewModel;
        }

        private void ShowListview()
        {
            CurrentViewModel = _listViewModel;
        }


        private void ShowAddView()
        {
            CurrentViewModel = _addViewModel;

        }

        private void ExitApp()
        {
            Application.Current.Shutdown();
        }
    }
}
