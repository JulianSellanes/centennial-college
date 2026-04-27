using System;
using System.IO;
using Microsoft.ML;
using Microsoft.ML.Data;

// Julian Sellanes (301494667)

namespace Exercise1_AIEngagement
{
    public class AiUsageData
    {
        [LoadColumn(0)]
        public string SessionID { get; set; }

        [LoadColumn(1)]
        public string StudentLevel { get; set; }

        [LoadColumn(2)]
        public string Discipline { get; set; }

        [LoadColumn(3)]
        public string SessionDate { get; set; }

        [LoadColumn(4)]
        public float SessionLengthMin { get; set; }

        [LoadColumn(5)]
        public float TotalPrompts { get; set; }

        [LoadColumn(6)]
        public string TaskType { get; set; }

        [LoadColumn(7)]
        public float AI_AssistanceLevel { get; set; }

        [LoadColumn(8)]
        public string FinalOutcome { get; set; }

        [LoadColumn(9)]
        public bool UsedAgain { get; set; }

        [LoadColumn(10)]
        public float SatisfactionRating { get; set; }
    }

    public class AiUsagePrediction
    {
        [ColumnName("PredictedLabel")]
        public bool WillUseAgain { get; set; }

        public float Probability { get; set; }

        public float Score { get; set; }
    }

    class Program
    {
        static readonly string _dataPath = Path.Combine(Environment.CurrentDirectory, "Data", "ai_assistant_usage_student_life.csv");

        static readonly string _modelPath = Path.Combine(Environment.CurrentDirectory, "Data", "AIEngagementModel.zip");

        static void Main(string[] args)
        {
            MLContext mlContext = new MLContext(seed: 1);

            IDataView dataView = mlContext.Data.LoadFromTextFile<AiUsageData>(path: _dataPath, hasHeader: true, separatorChar: ',');

            var splitData = mlContext.Data.TrainTestSplit(dataView, testFraction: 0.2);

            var pipeline =
                mlContext.Transforms.CopyColumns(outputColumnName: "Label", inputColumnName: nameof(AiUsageData.UsedAgain))
                .Append(mlContext.Transforms.Categorical.OneHotEncoding(new[]
                {
                    new InputOutputColumnPair("StudentLevelEncoded", nameof(AiUsageData.StudentLevel)),
                    new InputOutputColumnPair("DisciplineEncoded", nameof(AiUsageData.Discipline)),
                    new InputOutputColumnPair("TaskTypeEncoded", nameof(AiUsageData.TaskType)),
                    new InputOutputColumnPair("FinalOutcomeEncoded", nameof(AiUsageData.FinalOutcome))
                }))
                .Append(mlContext.Transforms.Concatenate("Features",
                    "StudentLevelEncoded",
                    "DisciplineEncoded",
                    "TaskTypeEncoded",
                    "FinalOutcomeEncoded",
                    nameof(AiUsageData.SessionLengthMin),
                    nameof(AiUsageData.TotalPrompts),
                    nameof(AiUsageData.AI_AssistanceLevel),
                    nameof(AiUsageData.SatisfactionRating)))
                .Append(mlContext.BinaryClassification.Trainers.SdcaLogisticRegression());

            ITransformer model = pipeline.Fit(splitData.TrainSet);

            IDataView predictions = model.Transform(splitData.TestSet);
            var metrics = mlContext.BinaryClassification.Evaluate(predictions);

            Console.WriteLine("===== Exercise 1: =====");
            Console.WriteLine($"Accuracy: {metrics.Accuracy:P2}");
            Console.WriteLine($"AUC:      {metrics.AreaUnderRocCurve:P2}");
            Console.WriteLine($"F1 Score: {metrics.F1Score:P2}");
            Console.WriteLine();

            using (var fs = new FileStream(_modelPath, FileMode.Create, FileAccess.Write, FileShare.Write))
            {
                mlContext.Model.Save(model, splitData.TrainSet.Schema, fs);
            }

            DataViewSchema modelSchema;
            ITransformer loadedModel = mlContext.Model.Load(_modelPath, out modelSchema);

            var predEngine = mlContext.Model.CreatePredictionEngine<AiUsageData, AiUsagePrediction>(loadedModel);

            var sample = new AiUsageData
            {
                SessionID = "SESSION99999",
                StudentLevel = "Undergraduate",
                Discipline = "Computer Science",
                SessionDate = "2025-06-01",
                SessionLengthMin = 25.0f,
                TotalPrompts = 8,
                TaskType = "Coding",
                AI_AssistanceLevel = 4,
                FinalOutcome = "Assignment Completed",
                UsedAgain = false,
                SatisfactionRating = 4.2f
            };

            var prediction = predEngine.Predict(sample);

            Console.WriteLine("Sample prediction:");
            Console.WriteLine($"Will use AI again? {prediction.WillUseAgain}");
            Console.WriteLine($"Probability:       {prediction.Probability:P2}");
        }
    }
}
