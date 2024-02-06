sudo apt update
sudo apt install openjdk-21-jdk
sudo apt install maven
sudo apt install postgresql
# Create database etym
sudo -u postgres psql -c "CREATE DATABASE wikt;"
# Set postgres password to "silver"
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'silver';"
