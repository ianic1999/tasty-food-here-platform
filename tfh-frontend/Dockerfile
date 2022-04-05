FROM node as build
WORKDIR /app
COPY package*.json /app/
RUN npm install

COPY . /app
RUN npm run build --prod

FROM nginx
COPY --from=build /app/dist/tfh-frontend /usr/share/nginx/html
EXPOSE 80
