using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace PMSModels
{
    public partial class PmsContext : DbContext
    {
        public PmsContext()
        {
        }

        public PmsContext(DbContextOptions<PmsContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Employee> Employees { get; set; }
        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<SalesTransaction> SalesTransactions { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlServer("Server=(localdb)\\MSSQLLocalDB;Database=PMS;Trusted_Connection=True;");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Employee>(entity =>
            {
                entity.ToTable("employee");

                entity.Property(e => e.Id)
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .HasColumnName("id");

                entity.Property(e => e.Email)
                    .HasMaxLength(100)
                    .IsUnicode(false)
                    .HasColumnName("Email");

                entity.Property(e => e.Fname)
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("fname");

                entity.Property(e => e.Lname)
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("lname");
            });

            modelBuilder.Entity<Product>(entity =>
            {
                entity.HasKey(e => e.Code);

                entity.ToTable("product");

                entity.Property(e => e.Code)
                    .HasMaxLength(7)
                    .IsUnicode(false)
                    .HasColumnName("code");

                entity.Property(e => e.Inventory).HasColumnName("inventory");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false)
                    .HasColumnName("name");

                entity.Property(e => e.Price)
                    .HasColumnType("decimal(18, 0)")
                    .HasColumnName("price");
            });

            modelBuilder.Entity<SalesTransaction>(entity =>
            {
                entity.HasKey(e => new { e.EmployeeId, e.ProductCode, e.SaleDate })
                    .HasName("pf");

                entity.ToTable("salesTransaction");

                entity.Property(e => e.EmployeeId)
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .HasColumnName("Employee_Id");

                entity.Property(e => e.ProductCode)
                    .HasMaxLength(7)
                    .IsUnicode(false)
                    .HasColumnName("product_code");

                entity.Property(e => e.SaleDate)
                    .HasColumnType("datetime")
                    .HasColumnName("saleDate");

                entity.Property(e => e.Amount).HasColumnName("amount");

                entity.HasOne(d => d.Employee)
                    .WithMany(p => p.SalesTransactions)
                    .HasForeignKey(d => d.EmployeeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_sale_emploee");

                entity.HasOne(d => d.ProductCodeNavigation)
                    .WithMany(p => p.SalesTransactions)
                    .HasForeignKey(d => d.ProductCode)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_sale_product");
            });
        }
    }
}
