using System;
using System.IO;
using Microsoft.ML;
using Microsoft.ML.Data;
using Microsoft.ML.Trainers;

// Julian Sellanes (301494667)

namespace Exercise3_StudentClustering
{
    public class StudentData
    {
        [LoadColumn(0)]
        public float STG { get; set; }

        [LoadColumn(1)]
        public float SCG { get; set; }

        [LoadColumn(2)]
        public float STR { get; set; }

        [LoadColumn(3)]
        public float LPR { get; set; }

        [LoadColumn(4)]
        public float PEG { get; set; }

        [LoadColumn(5)]
        public string UNS { get; set; }
    }

    public class ClusterPrediction
    {
        [ColumnName("PredictedLabel")]
        public uint PredictedClusterId { get; set; }

        [ColumnName("Score")]
        public float[] Distances { get; set; }
    }

    class Program
    {
        static readonly string _dataPath = Path.Combine(Environment.CurrentDirectory, "Data", "Student.csv");

        static readonly string _modelPath = Path.Combine(Environment.CurrentDirectory, "Data", "StudentClusteringModel.zip");

        static void Main(string[] args)
        {
            MLContext mlContext = new MLContext(seed: 1);

            IDataView dataView = mlContext.Data.LoadFromTextFile<StudentData>(path: _dataPath, hasHeader: true, separatorChar: ',');

            var options = new KMeansTrainer.Options
            {
                FeatureColumnName = "Features",
                NumberOfClusters = 4,
                MaximumNumberOfIterations = 100
            };

            var pipeline = mlContext.Transforms
                .Concatenate("Features",
                    nameof(StudentData.STG),
                    nameof(StudentData.SCG),
                    nameof(StudentData.STR),
                    nameof(StudentData.LPR),
                    nameof(StudentData.PEG))
                .Append(mlContext.Transforms.NormalizeMinMax("Features"))
                .Append(mlContext.Clustering.Trainers.KMeans(options));

            ITransformer model = pipeline.Fit(dataView);

            using (var fs = new FileStream(_modelPath, FileMode.Create, FileAccess.Write, FileShare.Write))
            {
                mlContext.Model.Save(model, dataView.Schema, fs);
            }

            DataViewSchema modelSchema;
            ITransformer loadedModel = mlContext.Model.Load(_modelPath, out modelSchema);

            var predEngine = mlContext.Model.CreatePredictionEngine<StudentData, ClusterPrediction>(loadedModel);

            var sample = new StudentData
            {
                STG = 0.10f,
                SCG = 0.10f,
                STR = 0.15f,
                LPR = 0.65f,
                PEG = 0.30f,
                UNS = ""
            };

            var prediction = predEngine.Predict(sample);

            Console.WriteLine("===== Exercise 3: =====");
            Console.WriteLine($"Predicted Cluster ID: {prediction.PredictedClusterId}");

            if (prediction.Distances != null)
            {
                Console.WriteLine("Distances to cluster centroids:");
                for (int i = 0; i < prediction.Distances.Length; i++)
                {
                    Console.WriteLine($"Cluster {i + 1}: {prediction.Distances[i]:0.####}");
                }
            }

            Console.WriteLine();
            Console.WriteLine("First few records with cluster assignments:");

            var transformedData = loadedModel.Transform(dataView);
            var rows = mlContext.Data.CreateEnumerable<StudentClusterResult>(transformedData, reuseRowObject: false);

            int count = 0;
            foreach (var row in rows)
            {
                Console.WriteLine($"STG={row.STG:0.##}, SCG={row.SCG:0.##}, STR={row.STR:0.##}, LPR={row.LPR:0.##}, PEG={row.PEG:0.##}, " + $"Original UNS={row.UNS}, Predicted Cluster={row.PredictedClusterId}");

                count++;
                if (count == 10)
                    break;
            }
        }
    }

    public class StudentClusterResult : StudentData
    {
        [ColumnName("PredictedLabel")]
        public uint PredictedClusterId { get; set; }
    }
}
