using System;
using System.IO;
using Microsoft.ML;
using Microsoft.ML.Data;

// Julian Sellanes (301494667)

namespace Exercise2_InsuranceRegression
{
    public class InsuranceData
    {
        [LoadColumn(0)]
        public float age { get; set; }

        [LoadColumn(1)]
        public string sex { get; set; }

        [LoadColumn(2)]
        public float bmi { get; set; }

        [LoadColumn(3)]
        public float children { get; set; }

        [LoadColumn(4)]
        public string smoker { get; set; }

        [LoadColumn(5)]
        public string region { get; set; }

        [LoadColumn(6)]
        public float charges { get; set; }
    }

    public class InsurancePrediction
    {
        [ColumnName("Score")]
        public float PredictedCharges { get; set; }
    }

    class Program
    {
        static readonly string _dataPath = Path.Combine(Environment.CurrentDirectory, "Data", "insurance.csv");

        static readonly string _modelPath = Path.Combine(Environment.CurrentDirectory, "Data", "InsuranceModel.zip");

        static void Main(string[] args)
        {
            MLContext mlContext = new MLContext(seed: 1);

            IDataView dataView = mlContext.Data.LoadFromTextFile<InsuranceData>(path: _dataPath, hasHeader: true, separatorChar: ',');

            var splitData = mlContext.Data.TrainTestSplit(dataView, testFraction: 0.2);

            var pipeline =
                mlContext.Transforms.CopyColumns(outputColumnName: "Label", inputColumnName: nameof(InsuranceData.charges))
                .Append(mlContext.Transforms.Categorical.OneHotEncoding(new[]
                {
                    new InputOutputColumnPair("SexEncoded", nameof(InsuranceData.sex)),
                    new InputOutputColumnPair("SmokerEncoded", nameof(InsuranceData.smoker)),
                    new InputOutputColumnPair("RegionEncoded", nameof(InsuranceData.region))
                }))
                .Append(mlContext.Transforms.Concatenate("Features",
                    nameof(InsuranceData.age),
                    nameof(InsuranceData.bmi),
                    nameof(InsuranceData.children),
                    "SexEncoded",
                    "SmokerEncoded",
                    "RegionEncoded"))
                .Append(mlContext.Regression.Trainers.FastTree());

            ITransformer model = pipeline.Fit(splitData.TrainSet);

            IDataView predictions = model.Transform(splitData.TestSet);
            var metrics = mlContext.Regression.Evaluate(predictions);

            Console.WriteLine("===== Exercise 2: =====");
            Console.WriteLine($"R-Squared: {metrics.RSquared:0.####}");
            Console.WriteLine($"RMSE:      {metrics.RootMeanSquaredError:0.####}");
            Console.WriteLine($"MAE:       {metrics.MeanAbsoluteError:0.####}");
            Console.WriteLine();

            using (var fs = new FileStream(_modelPath, FileMode.Create, FileAccess.Write, FileShare.Write))
            {
                mlContext.Model.Save(model, splitData.TrainSet.Schema, fs);
            }

            DataViewSchema modelSchema;
            ITransformer loadedModel = mlContext.Model.Load(_modelPath, out modelSchema);

            var predEngine = mlContext.Model.CreatePredictionEngine<InsuranceData, InsurancePrediction>(loadedModel);

            var sample = new InsuranceData
            {
                age = 29,
                sex = "female",
                bmi = 26.5f,
                children = 1,
                smoker = "no",
                region = "northeast",
                charges = 0
            };

            var prediction = predEngine.Predict(sample);

            Console.WriteLine("Sample prediction:");
            Console.WriteLine($"Predicted medical cost: {prediction.PredictedCharges:C2}");
        }
    }
}
