#DROP SCHEMA public CASCADE;
#CREATE SCHEMA public;
# execute with user postgres on the database wikt
sudo -u postgres psql -d wikt -c "DROP SCHEMA public CASCADE;"
sudo -u postgres psql -d wikt -c "CREATE SCHEMA public;"