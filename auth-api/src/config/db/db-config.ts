import { Sequelize } from "sequelize";

const sequelize = new Sequelize("auth-db", "postgres", "password", {
  host: "localhost",
  dialect: "postgres",
  quoteIdentifiers: false,
  define: {
    timestamps: true,
    underscored: true,
    freezeTableName: true,
  },
});

sequelize
  .authenticate()
  .then(() => {
    console.info("Connection has been established successfully.");
  })
  .catch((error) => {
    console.error("Unable to connect to the database:", error);
  });

export default sequelize;
