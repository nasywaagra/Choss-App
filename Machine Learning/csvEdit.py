import pandas as pd

def process_data(input_file, output_file):
    # Membaca data dari file CSV
    df = pd.read_csv(input_file)

    # Menghapus kolom Timestamp
    df = df.drop('Timestamp', axis=1)

    # Memisahkan Item Atasan Favorit menjadi Atasan1, Atasan2, Atasan3
    df[['Atasan1', 'Atasan2', 'Atasan3']] = df['Item Atasan Favorit '].str.split(', ', expand=True, n=2)

    # Memisahkan Item Bawahan Favorit menjadi Bawahan1, Bawahan2
    df[['Bawahan1', 'Bawahan2']] = df['Item Bawahan Favorit'].str.split(', ', expand=True, n=1)

    # Menghapus kolom Item Atasan Favorit dan Item Bawahan Favorit
    df = df.drop(['Item Atasan Favorit ', 'Item Bawahan Favorit'], axis=1)

    # Mengganti nama kolom Warna Pakaian Favorit menjadi 'Warna'
    df = df.rename(columns={'Warna Pakaian Favorit': 'Warna'})

    # Menyimpan hasil ke file CSV baru
    df.to_csv(output_file, index=True)

    print("SUCCESS")

if __name__ == "__main__":
    input_file_path = "E:/dataBangkit.csv"
    output_file_path = "E:/dataBangkit2.csv"
    process_data(input_file_path, output_file_path)
