import pandas as pd
import pyodbc

# =========================
# DB CONNECTION
# =========================
conn = pyodbc.connect(
    "DRIVER={SQL Server};"
    "SERVER=localhost;"
    "DATABASE=PSL_Auction;"
    "UID=sa;"
    "PWD=StrongPass@123;"
)

cursor = conn.cursor()

# =========================
# LOAD CSV
# =========================
df = pd.read_csv(r"D:\code-base\psl-auction\assets\PSL_2026_Auction_Dataset.csv")

# Clean columns (VERY IMPORTANT)
df.columns = df.columns.str.strip()

# =========================
# COLUMN MAPPING (IMPORTANT FIX)
# =========================
PLAYER_COL = "Player_Name"
TEAM_COL = "Franchise"
CATEGORY_COL = "Price_Category"

# =========================
# MAP STORAGE
# =========================
team_map = {}
category_map = {}
player_map = {}

# =========================================================
# 1. INSERT TEAMS
# =========================================================
for team in df[TEAM_COL].dropna().unique():
    cursor.execute(
        "INSERT INTO Team (Team_Name) VALUES (?)",
        team
    )

conn.commit()

cursor.execute("SELECT Team_ID, Team_Name FROM Team")
for row in cursor.fetchall():
    team_map[row.Team_Name] = row.Team_ID

# =========================================================
# 2. INSERT CATEGORIES
# =========================================================
cat_df = df[[CATEGORY_COL, "Base_Price_PKR_Cr"]].drop_duplicates()

for _, row in cat_df.iterrows():
    cursor.execute("""
        INSERT INTO Category (Category_Name, Base_Price)
        VALUES (?, ?)
    """,
    row[CATEGORY_COL],
    float(row["Base_Price_PKR_Cr"]) if pd.notna(row["Base_Price_PKR_Cr"]) else None
    )

conn.commit()

cursor.execute("SELECT Category_ID, Category_Name FROM Category")
for row in cursor.fetchall():
    category_map[row.Category_Name] = row.Category_ID

# =========================================================
# 3. INSERT PLAYERS
# =========================================================
for _, row in df.iterrows():

    name = row[PLAYER_COL]

    if name not in player_map:
        cursor.execute("""
            INSERT INTO Player (Player_Name, Nationality, Primary_Role)
            VALUES (?, ?, ?)
        """,
        name,
        row["Nationality"],
        row["Primary_Role"]
        )

        conn.commit()

        cursor.execute(
            "SELECT Player_ID FROM Player WHERE Player_Name = ?",
            name
        )
        player_map[name] = cursor.fetchone()[0]

# =========================================================
# 4. INSERT AUCTION
# =========================================================
for _, row in df.iterrows():

    player_id = player_map.get(row[PLAYER_COL])
    team_id = team_map.get(row[TEAM_COL])
    category_id = category_map.get(row[CATEGORY_COL])

    cursor.execute("""
        INSERT INTO Auction
        (Player_ID, Team_ID, Category_ID, Final_Price, Auction_Round, Acquisition_Type, Status)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """,
    player_id,
    team_id,
    category_id,
    float(row["Final_Price_PKR_Cr"]) if pd.notna(row["Final_Price_PKR_Cr"]) else None,
    row.get("Auction_Round"),
    row.get("Acquisition_Type"),
    row.get("Status")
    )

conn.commit()

# =========================================================
# 5. PLAYER CAREER (ONLY IF DATA EXISTS)
# =========================================================
for _, row in df.iterrows():

    player_id = player_map.get(row[PLAYER_COL])

    cursor.execute("""
        INSERT INTO PlayerCareer (
            Player_ID, PSL_Matches, Batting_Innings, Total_Runs,
            Highest_Score, Batting_Avg, Strike_Rate,
            Fifties, Hundreds, Wickets, Economy,
            Bowling_Avg, Best_Bowling
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """,
    player_id,
    row.get("Matches", 0),
    row.get("Innings", 0),
    row.get("Runs", 0),
    row.get("Highest_Score", 0),
    row.get("Batting_Avg", 0),
    row.get("Strike_Rate", 0),
    row.get("Fifties", 0),
    row.get("Hundreds", 0),
    row.get("Wickets", 0),
    row.get("Economy", 0),
    row.get("Bowling_Avg", 0),
    row.get("Best_Bowling", "0/0")
    )

conn.commit()

# =========================
# DONE
# =========================
cursor.close()
conn.close()

print("✅ Data successfully inserted into all ERD tables!")