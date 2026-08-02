import re

def compute_coherence(text):
    if not text or not text.strip():
        return 0.0
    sentences = [s.strip() for s in re.split(r'[.!?]', text) if s.strip()]
    if not sentences:
        return 0.0
    words = text.split()
    avg_len = len(words) / float(len(sentences))
    score = min(avg_len * 5.0, 100.0)
    return max(score, 0.0)
