# 🔒📱 BlockerCell  

*A smart call & SMS blocker to keep your privacy intact.*  

![BlockerCell Demo](https://via.placeholder.com/800x400?text=BlockerCell+Demo+GIF/Image) 

[![Python Version](https://img.shields.io/badge/Python-3.6%2B-blue)](https://www.python.org/)  
[![License: MIT](https://img.shields.io/badge/License-MIT-green)](LICENSE)  
[![GitHub Stars](https://img.shields.io/github/stars/Witthayanuraks/blockercell?style=social)](https://github.com/Witthayanuraks/blockercell)  

Tired of spam calls and unwanted messages? **BlockerCell** is your Python-powered shield against telemarketers, scammers, and annoying contacts. Block calls, filter SMS, and take back control of your phone! <br />
スパム電話や迷惑メッセージにうんざりしていませんか？**BlockerCell**は、テレマーケティング、詐欺師、迷惑な連絡先に対するPythonパワーの盾です。電話をブロックし、SMSをフィルタリングし、携帯電話のコントロールを取り戻しましょう！

---

## ✨ Features  

✅ **Call Blocking** – Silence specific numbers or unknown callers.  
✅ **SMS Filtering** – Auto-detect and block spam messages using keywords.  
✅ **Custom Rules** – Blacklist/whitelist numbers, regex patterns, or contacts.  
✅ **Easy to Use** – Simple CLI (GUI support planned? Mention if applicable).  
✅ **Cross-Platform** – Works on Windows, Linux, and Android (via Termux).  

---

## 🚀 Quick Start  

### Prerequisites  
- Python 3.6+  
- `pip`  

### Installation  
1. Clone & enter:  
   ```bash
   git clone https://github.com/Witthayanuraks/blockercell.git && cd blockercell
   ```
2. Install dependencies:  
   ```bash
   pip install -r requirements.txt
   ```
3. Run it:  
   ```bash
   python main.py --help
   ```

---

## 🛠️ Usage Examples  

### Block a number  
```bash
python main.py --block +1234567890
```  

### Unblock a number  
```bash
python main.py --unblock +1234567890
```  

### Filter SMS with keywords  
```bash
python main.py --filter-sms "spam,promo,loan"
```  

*(Add a GIF here showing the CLI in action!)*  

---

## ⚙️ Configuration  
Edit `config.json` to customize rules:  
```json
{
  "blacklist": ["+1234567890", "Scammer"],
  "whitelist_mode": false,
  "sms_keywords": ["spam", "urgent!"]
}
```

---

## 🤝 Contributing  
Love BlockerCell? Help make it better!  

1. **Fork** the repo.  
2. **Branch** your feature (`git checkout -b cool-feature`).  
3. **Commit** changes (`git commit -m 'Added cool feature'`).  
4. **Push** to the branch (`git push origin cool-feature`).  
5. **Open a PR** and describe your changes!  

---

## 📜 License  
MIT © [Aroes Noer Cahya](https://github.com/Witthayanuraks)  

---

## ❓ Need Help?  
- [Open an Issue](https://github.com/Witthayanuraks/blockercell/issues)  
- Reach out: **periodecode@gmail.co,**   

---

⭐ **Star this repo if you find it useful, and dont forget to still improve** 
