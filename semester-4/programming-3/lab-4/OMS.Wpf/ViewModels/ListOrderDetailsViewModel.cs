using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Input;
using OMS.Data.Dtos;
using OMS.Data.Services;
using OMS.Wpf.Commands;

namespace OMS.Wpf.ViewModels
{
    public class ListOrderDetailsViewModel : BaseViewModel
    {
        private readonly OmsService _omsService;
        private readonly Action<int?> _onAddRequested;

        public ObservableCollection<BasketComboItemDto> Baskets { get; } =
            new ObservableCollection<BasketComboItemDto>();

        public ObservableCollection<BasketItemDetailDto> BasketItems { get; } =
            new ObservableCollection<BasketItemDetailDto>();

        private BasketComboItemDto _selectedBasket;
        public BasketComboItemDto SelectedBasket
        {
            get => _selectedBasket;
            set
            {
                if (SetProperty(ref _selectedBasket, value))
                {
                    _ = LoadBasketItemsAsync();
                }
            }
        }

        public ICommand AddItemToOrderCommand { get; }

        public ListOrderDetailsViewModel(OmsService omsService, Action<int?> onAddRequested)
        {
            _omsService = omsService;
            _onAddRequested = onAddRequested;

            AddItemToOrderCommand = new RelayCommand(_ => OpenAddView());

            _ = ReloadAsync();
        }

        private void OpenAddView()
        {
            _onAddRequested?.Invoke(SelectedBasket?.IdBasket);
        }

        public async Task ReloadAsync()
        {
            int? currentBasketId = SelectedBasket?.IdBasket;

            var basketList = await _omsService.GetBasketsAsync();

            Baskets.Clear();
            foreach (var basket in basketList)
            {
                Baskets.Add(basket);
            }

            if (Baskets.Count == 0)
            {
                BasketItems.Clear();
                SelectedBasket = null;
                return;
            }

            SelectedBasket = Baskets.FirstOrDefault(b => b.IdBasket == currentBasketId)
                             ?? Baskets.First();
        }

        private async Task LoadBasketItemsAsync()
        {
            BasketItems.Clear();

            if (SelectedBasket == null)
                return;

            var items = await _omsService.GetBasketItemsByBasketIdAsync(SelectedBasket.IdBasket);

            foreach (var item in items)
            {
                BasketItems.Add(item);
            }
        }
    }
}