<#
  Jalankan test satu quest. Kalau hijau, commit file solusi quest itu.

  Pakai:  .\quest F04            (satu quest)
          .\quest F00 F01 F02    (beberapa, satu commit per quest)
          .\quest F04 -Push      (commit lalu push ke origin)
#>
param(
    [Parameter(Mandatory = $true, ValueFromRemainingArguments = $true)]
    [string[]] $Ids,
    [switch] $Push
)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$fondasi = 'src/main/java/com/alterego/fondasi'
$warmup  = 'src/main/java/com/alterego/warmup'

# Class yang dipakai test tiap quest (termasuk class yang kamu buat sendiri).
$files = @{
    F00 = @("$fondasi/Hello.java")
    F01 = @("$fondasi/MatchMath.java")
    F02 = @("$fondasi/Ranks.java")
    F03 = @("$fondasi/Names.java")
    F04 = @("$fondasi/Scoring.java")
    F05 = @("$fondasi/Scoreboard.java", "$fondasi/MatchResult.java")
    F06 = @("$fondasi/Lineup.java")
    F07 = @("$fondasi/HeroPicks.java", "$fondasi/HeroPool.java")
    F08 = @("$fondasi/Finder.java")
    F09 = @("$fondasi/Parser.java", "$fondasi/ScoreFormatException.java")
    F10 = @("$fondasi/Entry.java", "$fondasi/Leaderboard.java")
    F11 = @("$fondasi/Candidate.java", "$fondasi/Rule.java", "$fondasi/Rules.java", "$fondasi/NicknameRule.java")
    F12 = @("$fondasi/Match.java", "$fondasi/MatchStats.java")
    F13 = @("$fondasi/Boxes.java", "$fondasi/Pair.java", "$fondasi/TypedBag.java")
    Q01 = @("$warmup/Slugs.java")
    Q02 = @("$warmup/RosterBook.java", "$warmup/WarmupPlayer.java")
    Q03 = @("$warmup/AchievementMath.java", "$warmup/WarmupAchievement.java")
    Q04 = @("$warmup/Validator.java", "$warmup/PlayerValidator.java", "$warmup/CompositeValidator.java")
    Q05 = @("$warmup/PriceParser.java", "$warmup/PriceRange.java", "$warmup/InvalidPriceException.java")
    Q06 = @("$warmup/Required.java", "$warmup/RequiredFieldChecker.java")
    Q07 = @("$warmup/MiniContainer.java")
}
# Q08-Q26 (Spring): semua perubahan di sini ikut commit.
$springPaths = @('src/main', 'pom.xml')

function Get-QuestTitle([string] $id) {
    $test = Get-ChildItem -Path 'src/test/java' -Recurse -Filter "${id}_*Test.java" | Select-Object -First 1
    if (-not $test) { return $null }
    $line = Get-Content $test.FullName -Encoding UTF8 | Where-Object { $_ -match '@DisplayName\("' } | Select-Object -First 1
    # [char]0xB7 = titik tengah pada "F00 <titik> Judul"; skrip ini harus tetap ASCII untuk PowerShell 5.1.
    if ($line -match ('@DisplayName\("[^"]*?' + [char]0xB7 + '\s*([^"]+)"\)')) { return $Matches[1].Trim() }
    return $id
}

$committed = 0
foreach ($raw in $Ids) {
    $id = $raw.ToUpper()
    if ($id -notmatch '^[FQ]\d\d$') { Write-Host "Format ID salah: $raw (contoh: F04, Q12)" -ForegroundColor Red; exit 1 }
    $title = Get-QuestTitle $id
    if (-not $title) { Write-Host "Test untuk $id tidak ditemukan." -ForegroundColor Red; exit 1 }

    Write-Host "`n== $id - $title ==" -ForegroundColor Cyan
    & .\mvnw.cmd -q test "-Dtest=${id}_*"
    if ($LASTEXITCODE -ne 0) {
        Write-Host "$id masih merah. Tidak ada commit. Baca error di atas." -ForegroundColor Red
        exit 1
    }
    Write-Host "$id hijau." -ForegroundColor Green

    $paths = if ($files.ContainsKey($id)) { $files[$id] | Where-Object { Test-Path $_ } } else { $springPaths }
    git add -- $paths
    git diff --cached --quiet -- $paths
    if ($LASTEXITCODE -eq 0) { Write-Host "Tidak ada perubahan baru untuk $id, lewati commit." -ForegroundColor Yellow; continue }

    $msgFile = [IO.Path]::GetTempFileName()
    [IO.File]::WriteAllText($msgFile, "solve(${id}): $title`n", (New-Object Text.UTF8Encoding $false))
    git commit -q -F $msgFile -- $paths
    Remove-Item $msgFile
    if ($LASTEXITCODE -ne 0) { exit 1 }
    git --no-pager log -1 --stat --format='%h %s'
    $committed++
}

if ($Push -and $committed -gt 0) { git push -q; Write-Host "Sudah di-push." -ForegroundColor Green }
