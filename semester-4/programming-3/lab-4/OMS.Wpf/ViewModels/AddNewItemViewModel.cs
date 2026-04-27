using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using OMS.Data.Dtos;
using OMS.Data.Services;
using OMS.Wpf.Commands;

namespace OMS.Wpf.ViewModels
{
    public class AddNewItemViewModel : BaseViewModel
    {
        private readonly OmsService _omsService;
        private readonly Func<Task> _onSaved;
        private readonly Action _onCancelled;

        public ObservableCollection<BasketComboItemDto> Baskets { get; } =
            new ObservableCollection<BasketComboItemDto>();

        public ObservableCollection<ProductComboItemDto> Products { get; } =
            new ObservableCollection<ProductComboItemDto>();

        private BasketComboItemDto _selectedBasket;
        public BasketComboItemDto SelectedBasket
        {
            get => _selectedBasket;
            set => SetProperty(ref _selectedBasket, value);
        }

        private ProductComboItemDto _selectedProduct;
        public ProductComboItemDto SelectedProduct
        {
            get => _selectedProduct;
            set => SetProperty(ref _selectedProduct, value);
        }

        private string _quantityText = "1";
        public string QuantityText
        {
            get => _quantityText;
            set => SetProperty(ref _quantityText, value);
        }

        public ICommand SaveCommand { get; }
        public ICommand CancelCommand { get; }

        public AddNewItemViewModel(OmsService omsService, Func<Task> onSaved, Action onCancelled)
        {
            _omsService = omsService;
            _onSaved = onSaved;
            _onCancelled = onCancelled;

            SaveCommand = new RelayCommand(async _ => await SaveAsync());
            CancelCommand = new RelayCommand(_ => Cancel());

            _ = LoadReferenceDataAsync();
        }

        public async Task PrepareForNewItemAsync(int? preferredBasketId)
        {
            await LoadReferenceDataAsync();

            if (preferredBasketId.HasValue)
            {
                SelectedBasket = Baskets.FirstOrDefault(b => b.IdBasket == preferredBasketId.Value)
                                 ?? Baskets.FirstOrDefault();
            }
            else
            {
                SelectedBasket = Baskets.FirstOrDefault();
            }

            if (SelectedProduct == null)
                SelectedProduct = Products.FirstOrDefault();

            QuantityText = "1";
        }

        private async Task LoadReferenceDataAsync()
        {
            var baskets = await _omsService.GetBasketsAsync();
            var products = await _omsService.GetProductsAsync();

            Baskets.Clear();
            foreach (var basket in baskets)
            {
                Baskets.Add(basket);
            }

            Products.Clear();
            foreach (var product in products)
            {
                Products.Add(product);
            }

            if (SelectedBasket == null)
                SelectedBasket = Baskets.FirstOrDefault();

            if (SelectedProduct == null)
                SelectedProduct = Products.FirstOrDefault();
        }

        private async Task SaveAsync()
        {
            try
            {
                if (SelectedBasket == null)
                {
                    MessageBox.Show("Please select a basket.");
                    return;
                }

                if (SelectedProduct == null)
                {
                    MessageBox.Show("Please select a product.");
                    return;
                }

                if (!byte.TryParse(QuantityText, out byte quantity) || quantity == 0)
                {
                    MessageBox.Show("Please enter a valid quantity greater than 0.");
                    return;
                }

                var dto = new AddBasketItemDto
                {
                    IdBasket = SelectedBasket.IdBasket,
                    IdProduct = SelectedProduct.IdProduct,
                    Quantity = quantity
                };

                int newId = await _omsService.AddBasketItemAsync(dto);

                MessageBox.Show($"Item saved successfully. New IdBasketItem = {newId}");

                if (_onSaved != null)
                    await _onSaved();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error saving item:\n" + ex.Message);
            }
        }

        private void Cancel()
        {
            _onCancelled?.Invoke();
        }
    }
}