using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

#nullable disable

namespace OMS.Data.Models
{
    public partial class OMSContext : DbContext
    {
        public OMSContext()
        {
        }

        public OMSContext(DbContextOptions<OMSContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Basket> Baskets { get; set; }
        public virtual DbSet<BasketItem> BasketItems { get; set; }
        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<Shopper> Shoppers { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
                optionsBuilder.UseSqlServer("Server=(localdb)\\MSSQLLocalDB;Database=OMS;Trusted_Connection=True;");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasAnnotation("Relational:Collation", "SQL_Latin1_General_CP1_CI_AS");

            modelBuilder.Entity<Basket>(entity =>
            {
                entity.HasKey(e => e.IdBasket)
                    .HasName("PK__Basket__7ADD396BE0ECAF72");

                entity.Property(e => e.IdBasket).ValueGeneratedNever();

                entity.HasOne(d => d.IdShopperNavigation)
                    .WithMany(p => p.Baskets)
                    .HasForeignKey(d => d.IdShopper)
                    .HasConstraintName("bskt_idshopper_fk");
            });

            modelBuilder.Entity<BasketItem>(entity =>
            {
                entity.HasKey(e => e.IdBasketItem)
                    .HasName("PK__BasketIt__2B9ACCF577D31DE4");

                entity.Property(e => e.IdBasketItem).ValueGeneratedNever();

                entity.HasOne(d => d.IdBasketNavigation)
                    .WithMany(p => p.BasketItems)
                    .HasForeignKey(d => d.IdBasket)
                    .HasConstraintName("bsktitem_bsktid_fk");

                entity.HasOne(d => d.IdProductNavigation)
                    .WithMany(p => p.BasketItems)
                    .HasForeignKey(d => d.IdProduct)
                    .HasConstraintName("bsktitem_idprod_fk");
            });

            modelBuilder.Entity<Product>(entity =>
            {
                entity.HasKey(e => e.IdProduct)
                    .HasName("PK__Product__5EEC79D16DF1FB8B");

                entity.Property(e => e.IdProduct).ValueGeneratedNever();

                entity.Property(e => e.Description).IsUnicode(false);

                entity.Property(e => e.ProductName).IsUnicode(false);
            });

            modelBuilder.Entity<Shopper>(entity =>
            {
                entity.HasKey(e => e.IdShopper)
                    .HasName("PK__Shopper__14739D52D8A0B32E");

                entity.Property(e => e.IdShopper).ValueGeneratedNever();

                entity.Property(e => e.Address).IsUnicode(false);

                entity.Property(e => e.City).IsUnicode(false);

                entity.Property(e => e.Country).IsUnicode(false);

                entity.Property(e => e.Email).IsUnicode(false);

                entity.Property(e => e.FirstName).IsUnicode(false);

                entity.Property(e => e.LastName).IsUnicode(false);

                entity.Property(e => e.StateProvince).IsUnicode(false);

                entity.Property(e => e.ZipCode).IsUnicode(false);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
