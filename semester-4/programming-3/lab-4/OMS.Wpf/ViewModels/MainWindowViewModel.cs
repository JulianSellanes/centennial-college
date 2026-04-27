using System.Threading.Tasks;
using OMS.Data.Services;

namespace OMS.Wpf.ViewModels
{
    public class MainWindowViewModel : BaseViewModel
    {
        public ListOrderDetailsViewModel ListOrderDetailsViewModel { get; }
        public AddNewItemViewModel AddNewItemViewModel { get; }

        private object _currentViewModel;
        public object CurrentViewModel
        {
            get => _currentViewModel;
            set => SetProperty(ref _currentViewModel, value);
        }

        public MainWindowViewModel()
        {
            ListOrderDetailsViewModel = new ListOrderDetailsViewModel(
                new OmsService(),
                ShowAddView);

            AddNewItemViewModel = new AddNewItemViewModel(
                new OmsService(),
                OnItemSavedAsync,
                ShowListView);

            CurrentViewModel = ListOrderDetailsViewModel;
        }

        private async void ShowAddView(int? selectedBasketId)
        {
            await AddNewItemViewModel.PrepareForNewItemAsync(selectedBasketId);
            CurrentViewModel = AddNewItemViewModel;
        }

        private void ShowListView()
        {
            CurrentViewModel = ListOrderDetailsViewModel;
        }

        private async Task OnItemSavedAsync()
        {
            await ListOrderDetailsViewModel.ReloadAsync();
            CurrentViewModel = ListOrderDetailsViewModel;
        }
    }
}