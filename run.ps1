# Le o arquivo .env desta pasta, exporta as variaveis para a sessao atual
# do PowerShell e roda o backend em seguida.
#
# Uso: dentro da pasta backend, rode:
#   .\run.ps1
#
# Se der erro de "nao e possivel executar scripts neste sistema", rode:
#   powershell -ExecutionPolicy Bypass -File .\run.ps1

$envFile = Join-Path $PSScriptRoot ".env"

if (-not (Test-Path $envFile)) {
    Write-Host "Arquivo .env nao encontrado em $envFile" -ForegroundColor Yellow
    Write-Host "Crie um .env (baseado no .env.example) antes de continuar." -ForegroundColor Yellow
    exit 1
}

Get-Content $envFile | ForEach-Object {
    $linha = $_.Trim()
    if ($linha -eq "" -or $linha.StartsWith("#")) { return }

    $partes = $linha -split "=", 2
    if ($partes.Length -eq 2) {
        $nome = $partes[0].Trim()
        $valor = $partes[1].Trim().Trim("'").Trim('"')
        [System.Environment]::SetEnvironmentVariable($nome, $valor, "Process")
        Write-Host "Variavel carregada: $nome" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "Iniciando o backend..." -ForegroundColor Cyan
mvn spring-boot:run
