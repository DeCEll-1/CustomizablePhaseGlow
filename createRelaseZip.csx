#r "System.Security.Cryptography"
#r "System.IO"
#r "System.IO.Compression"

using System;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Security.Cryptography;

string projectRoot = Environment.CurrentDirectory;

string[] itemsToZip = File.ReadAllLines(Path.Combine(projectRoot, "paths.txt"));

string outputDirectory = projectRoot;
string releaseFileName = "CPG.zip";
string zipRootFolder = "CPG";
string destinationZip = Path.Combine(outputDirectory, releaseFileName);

public static string GetFileHash(string filePath)
{
    using (SHA256 sha256 = SHA256.Create())
    {
        using (FileStream stream = File.OpenRead(filePath))
        {
            byte[] hashBytes = sha256.ComputeHash(stream);

            // Convert byte array to a clean hexadecimal string
            return BitConverter.ToString(hashBytes).Replace("-", "").ToLowerInvariant();
        }
    }
}

public class TeeWriter : TextWriter
{
    private readonly TextWriter _consoleOut;
    private readonly StringWriter _stringWriter;

    public override System.Text.Encoding Encoding => _consoleOut.Encoding;

    public TeeWriter(TextWriter consoleOut, StringWriter stringWriter)
    {
        _consoleOut = consoleOut;
        _stringWriter = stringWriter;
    }

    public override void Write(string value)
    {
        _consoleOut.Write(value);
        _stringWriter.Write(value);
    }

    public override void WriteLine(string value)
    {
        _consoleOut.WriteLine(value);
        _stringWriter.WriteLine(value);
    }
    
    public override void Write(char value)
    {
        _consoleOut.Write(value);
        _stringWriter.Write(value);
    }

    public override String ToString()
    {
        return _stringWriter.ToString();
    }
}


StringWriter sw = new StringWriter();
TeeWriter tee = new TeeWriter(Console.Out, sw);
Console.SetOut(tee);

Console.WriteLine(nameof(projectRoot) + " : " + projectRoot);
Console.WriteLine(nameof(itemsToZip) + " : [" + string.Join(", ", itemsToZip) + "]");
Console.WriteLine(nameof(releaseFileName) + " : " + releaseFileName);
Console.WriteLine(nameof(zipRootFolder) + " : " + zipRootFolder);
Console.WriteLine(nameof(destinationZip) + " : " + destinationZip);

try
{
    if (File.Exists(destinationZip))
        File.Delete(destinationZip);

    using MemoryStream memoryStream = new MemoryStream();
    using ZipArchive archive = new ZipArchive(memoryStream, ZipArchiveMode.Create, true);
    foreach (string item in itemsToZip)
    {
        string sourcePath = Path.Combine(projectRoot, item);

        if (Directory.Exists(sourcePath))
        {
            Console.ForegroundColor = ConsoleColor.Magenta;
            Console.WriteLine($"Writing {item}");
            Console.ResetColor();

            string lastPrintedFolder = string.Empty;
            string[] files = Directory.GetFiles(sourcePath, "*", SearchOption.AllDirectories);

            foreach (string file in files)
            {
                string currentFolder = Path.GetDirectoryName(file);

                if (currentFolder != lastPrintedFolder)
                {
                    Console.ForegroundColor = ConsoleColor.Yellow;
                    Console.WriteLine($"Writing on: {Path.GetRelativePath(projectRoot, currentFolder).Replace(@"\", "/")}");
                    Console.ResetColor();
                    lastPrintedFolder = currentFolder;
                }

                string relativePath = Path.GetRelativePath(projectRoot, file).Replace('\\', '/');

                string entryPath = $"{zipRootFolder}/{relativePath}";

                var zipEntry = archive.CreateEntry(entryPath, CompressionLevel.Optimal);

                using Stream entryStream = zipEntry.Open();
                using FileStream fileStream = File.OpenRead(file);
                fileStream.CopyTo(entryStream);
                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write($"Writing ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(file.Replace(currentFolder, "").Replace(@"\", "/"));
                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write(" as ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(entryPath);
                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write(" with hash ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(GetFileHash(file));
                Console.WriteLine();
                Console.ResetColor();
            }
        }
        else
        {
            if (File.Exists(item))
            {
                string relativePath = Path.GetRelativePath(projectRoot, item).Replace('\\', '/');

                string entryPath = $"{zipRootFolder}/{relativePath}";

                var zipEntry = archive.CreateEntry(entryPath, CompressionLevel.Optimal);

                using Stream entryStream = zipEntry.Open();
                using FileStream fileStream = File.OpenRead(item);
                fileStream.CopyTo(entryStream);

                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write($"Writing ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(item);
                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write(" as ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(entryPath);
                Console.ForegroundColor = ConsoleColor.Green;
                Console.Write(" with hash ");
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.Write(GetFileHash(item));
                Console.WriteLine();
                Console.ResetColor();
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"{item} not found.");
                Console.ResetColor();
            }
        }
    }

    var metaEntry = archive.CreateEntry($"{zipRootFolder}/metadata.txt");
    using (StreamWriter writer = new StreamWriter(metaEntry.Open()))
    {
        writer.Write(tee.ToString());
    }

    archive.Dispose();

    tee.Dispose();

    byte[] zipBytes = memoryStream.ToArray();
    memoryStream.Close();
    memoryStream.Dispose();

    if (zipBytes.Length > 0)
    {
        Console.ForegroundColor = ConsoleColor.Cyan;
        Console.WriteLine("Writing compiled memory buffer out to disk...");
        Console.ResetColor();

        File.WriteAllBytes(destinationZip, zipBytes.ToArray());

        Console.ForegroundColor = ConsoleColor.Green;
        Console.WriteLine($"Generated at: {destinationZip}");
        Console.ResetColor();
    }
    else
    {
        Console.ForegroundColor = ConsoleColor.Red;
        Console.WriteLine("Build failed: No files were streamed into the memory matrix.");
        Console.ResetColor();
    }
}
catch (Exception ex)
{
    Console.ForegroundColor = ConsoleColor.Red;
    Console.WriteLine($"An error occurred during packing: {ex.Message}");
    Console.ResetColor();
}
finally
{
    // 3. Always restore the original console
    Console.SetOut(new StreamWriter(Console.OpenStandardOutput()) { AutoFlush = true });
}

Console.WriteLine("Press any key to quit");
Console.ReadKey();