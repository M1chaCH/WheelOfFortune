FROM node:lts-alpine3.14 as wheeloffortune-frontend-build

COPY . /var/local/WheelOfFortune/frontend/
WORKDIR /var/local/WheelOfFortune/frontend/

RUN npm install
RUN npm run build

FROM nginx:stable

COPY --from=wheeloffortune-frontend-build /var/local/WheelOfFortune/frontend/dist/frontend /usr/share/nginx/html

EXPOSE 80
EXPOSE 443