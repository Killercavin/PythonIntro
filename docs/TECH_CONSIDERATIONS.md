# 🛠 DevHatch – Tech Stack Considerations

This document outlines our current thoughts on what technologies might power DevHatch. Nothing is final — if you’ve got ideas, pitch in!

---

## Frontend Options
- ✅ Next.js (great for SSR, Vercel deploy), Nuxt.js (SSR)
- React + Vite
- SvelteKit
- Vue 3 + Vite
- Angular

## Backend Options
- ✅ Node.js + Express (lightweight, GitHub API friendly), Ktor, Spring Boot, Django, Flask, NestJS
- FastAPI (Python for future intelligent agent plugins)
- Firebase, Supabase (serverless MVP)

## Realtime Support
- Socket.io (team updates + CollabBoard)
- Firebase RTDB or Pusher

## Database
- ✅ MongoDB Atlas (document-oriented, flexible)
- PostgreSQL (optional)

## Dev Tools
- GitHub Actions (CI/CD)
- Husky + Lint Staged (pre-commit hooks)
- ESLint, Prettier, Conventional Commits

---

> 🚧 Final stack will be decided once MVP features are scoped out.
