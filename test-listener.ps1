$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add("http://localhost:8080/")
try {
    $listener.Start()
    Write-Host "Listening: $($listener.IsListening)"
    $listener.Stop()
} catch {
    Write-Host "Failed: $_"
}
