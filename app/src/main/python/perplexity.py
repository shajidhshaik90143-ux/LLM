def compute_perplexity(text):
    if not text or not text.strip():
        return 0.0
    words = text.split()
    if not words:
        return 0.0
    unique = len(set(words))
    ratio = unique / float(len(words))
    return min(max(ratio * 100.0, 0.0), 100.0)
