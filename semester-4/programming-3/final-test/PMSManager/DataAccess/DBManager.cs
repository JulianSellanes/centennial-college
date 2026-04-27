using Microsoft.EntityFrameworkCore;
using PMSManager.Models;
using PMSModels;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace PMSManager.DataAccess
{
    public class DBManager
    {
        public async Task<ObservableCollection<SalesPersonComboBoxModel>> GetSalespersonsAsync()
        {
            using var db = new PmsContext();

            var list = await db.Employees
                .OrderBy(e => e.Id)
                .Select(e => new SalesPersonComboBoxModel(e.Id, e.Fname, e.Lname))
                .ToListAsync();

            return new ObservableCollection<SalesPersonComboBoxModel>(list);
        }

        public async Task<ObservableCollection<SalesDetails>> GetTransactionsByEmployeeAsync(string employeeId)
        {
            using var db = new PmsContext();

            var list = await db.SalesTransactions
                .Where(t => t.EmployeeId == employeeId)
                .Select(t => new SalesDetails(
                    t.ProductCode,
                    t.ProductCodeNavigation.Name,
                    t.Amount,
                    t.SaleDate))
                .ToListAsync();

            return new ObservableCollection<SalesDetails>(list);
        }

        public async Task<ObservableCollection<ProductComboBoxModel>> GetProductsAsync()
        {
            using var db = new PmsContext();

            var list = await db.Products
                .OrderBy(p => p.Code)
                .Select(p => new ProductComboBoxModel(p.Code, p.Name))
                .ToListAsync();

            return new ObservableCollection<ProductComboBoxModel>(list);
        }

        public async Task AddTransactionAsync(string employeeId, string productCode, int amount, DateTime saleDate)
        {
            using var db = new PmsContext();

            var transaction = new SalesTransaction
            {
                EmployeeId = employeeId,
                ProductCode = productCode,
                Amount = amount,
                SaleDate = saleDate
            };

            db.SalesTransactions.Add(transaction);
            await db.SaveChangesAsync();
        }
    }
}
