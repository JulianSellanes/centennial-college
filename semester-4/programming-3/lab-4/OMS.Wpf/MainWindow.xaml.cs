using System.Windows;
using OMS.Wpf.ViewModels;

// Julian Sellanes (301494667)

namespace OMS.Wpf
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            DataContext = new MainWindowViewModel();
        }
    }
}
