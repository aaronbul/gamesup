#!/usr/bin/env python3
"""
Script de démarrage simple pour l'API de recommandation
"""

import uvicorn
import sys
import os

# Ajouter le répertoire actuel au path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

if __name__ == "__main__":
    print("🚀 Démarrage de l'API de recommandation GamesUP...")
    print("📍 URL: http://localhost:8001")
    print("📚 Documentation: http://localhost:8001/docs")
    print("=" * 50)
    
    try:
        uvicorn.run(
            "main:app",
            host="127.0.0.1",
            port=8001,
            log_level="info",
            reload=False
        )
    except KeyboardInterrupt:
        print("\n👋 API arrêtée par l'utilisateur")
    except Exception as e:
        print(f"❌ Erreur lors du démarrage: {e}")
        sys.exit(1) 