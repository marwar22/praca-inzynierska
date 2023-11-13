FROM node:20-alpine AS frontend-build

WORKDIR /frontend

COPY package*.json ./

RUN npm ci

RUN npm audit fix

COPY . .

RUN npm run build

FROM node:20-alpine

WORKDIR /frontend

COPY --from=frontend-build /frontend/package*.json /frontend/

COPY --from=frontend-build /frontend/.output /frontend/.output

EXPOSE 3000 

ENTRYPOINT ["npm", "run", "start"]
